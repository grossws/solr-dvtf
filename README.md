# Info


[DocValues][solr-dv]-enabled [TextField][solr-tf] for [Apache Solr][solr].

- [DocValuesTextField](src/java/org/anenerbe/solr/DocValuesTextField.java) (which is subclass of [solr.TextField][solr-tf])

[solr]: http://lucene.apache.org/solr/
[solr-tf]: http://lucene.apache.org/solr/api/solr-core/org/apache/solr/schema/TextField.html
[solr-dv]: https://cwiki.apache.org/confluence/display/solr/DocValues

# Usage

## DocValuesTextField

Add some fieldTypes to solr `schema.xml` (and fields using them):

```xml
<schema name="example" version="1.5">
  <!-- ... -->

  <field name="ne" type="ne_string"/>
  <field name="nef" type="nef_string"/>
  <copyField source="ne" dest="nef"/>

  <!-- ... -->

  <fieldType name="nef_string" class="org.anenerbe.solr.DocValuesTextField"
             indexed="false" stored="false" multiValued="true" docValues="true">
    <analyzer type="index">
      <tokenizer class="solr.KeywordTokenizerFactory"/>
      <filter class="solr.ASCIIFoldingFilterFactory"/>
      <filter class="solr.LowerCaseFilterFactory"/>
    </analyzer>
    <analyzer type="query">
      <tokenizer class="solr.KeywordTokenizerFactory"/>
      <filter class="solr.ASCIIFoldingFilterFactory"/>
      <filter class="solr.LowerCaseFilterFactory"/>
    </analyzer>
  </fieldType>

  <fieldType name="ne_string" class="org.anenerbe.solr.DocValuesTextField"
             indexed="true" stored="true" multiValued="true" docValues="true">
    <analyzer type="index">
      <tokenizer class="solr.KeywordTokenizerFactory"/>
      <filter class="solr.ASCIIFoldingFilterFactory"/>
      <filter class="solr.LowerCaseFilterFactory"/>
      <filter class="solr.ReversedWildcardFilterFactory"/>
    </analyzer>
    <analyzer type="query">
      <tokenizer class="solr.KeywordTokenizerFactory"/>
      <filter class="solr.ASCIIFoldingFilterFactory"/>
      <filter class="solr.LowerCaseFilterFactory"/>
    </analyzer>
  </fieldType>

  <!-- ... -->
</schema>
```


# Licensing

Licensed under MIT License. See [LICENSE](LICENSE) file.

