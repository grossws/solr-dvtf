package org.anenerbe.solr;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrException;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * solr.TextField with enabled docValues
 * <p>
 * This implementation ensures that tokenizer and token filter chain produces
 * only one token if field is single-valued, so its result can be stored
 * in {@link SortedDocValuesField}.
 * <p>
 * If field is multi-valued, it's stored in {@link SortedSetDocValuesField}.
 * <p>
 * {@code docValues} are enabled implicitly and specifying {@code docValues="false"} will lead to exception.
 * <p>
 * By default field is multi-valued (if not explicitly set otherwise).
 */
public class DocValuesTextField extends TextField {
  @Override
  protected void init(IndexSchema schema, Map<String, String> args) {
    // fail if docValues==false
    if ((falseProperties & DOC_VALUES) != 0) {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, getClass().getName() + " requires docValues==true");
    }
    // force docValues
    properties |= DOC_VALUES;
    // enable multiValued if not explicitly set to false
    if ((falseProperties & MULTIVALUED) == 0) {
      properties |= MULTIVALUED;
    }

    super.init(schema, args);
  }

  @Override
  public List<IndexableField> createFields(SchemaField field, Object value) {
    if (field.hasDocValues()) {
      List<IndexableField> fields = new ArrayList<>();
      fields.add(createField(field, value));

      List<String> data = analyzedField(field, value);
      if (field.multiValued()) {
        for (String datum : data) {
          final BytesRef bytes = new BytesRef(datum);
          fields.add(new SortedSetDocValuesField(field.getName(), bytes));
        }
      } else {
        if (data.size() > 1) {
          throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Field analysis for " + field + " returned multiple analyzed values");
        }
        final BytesRef bytes = new BytesRef(data.get(0));
        fields.add(new SortedDocValuesField(field.getName(), bytes));
      }

      return fields;
    } else {
      return Collections.singletonList(createField(field, value));
    }
  }

  @Override
  public void checkSchemaField(SchemaField field) {
  }

  /**
   * For correct {@link org.apache.solr.request.DocValuesFacets} work.
   * <p>
   * {@inheritDoc}
   *
   * @return {@code true} if field has effectively more than one value
   */
  @Override
  public boolean multiValuedFieldCache() {
    return isMultiValued();
  }

  private List<String> analyzedField(SchemaField field, Object value) {
    try {
      List<String> result = new ArrayList<>();
      TokenStream ts = field.getType().getIndexAnalyzer().tokenStream(field.getName(), value.toString());
      CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
      try {
        ts.reset();
        while (ts.incrementToken()) {
          result.add(term.toString());
        }
        ts.end();
      } finally {
        ts.close();
      }
      return result;
    } catch (IOException e) {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Can't analyze " + value.toString() + " in " + field);
    }
  }
}
