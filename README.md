Parquet Table Generator
============================

Proof of concept project for generating:
- wide Parquet partitioned table containing a lot of columns.
- long Parquet partitioned table containing a lot of partitions.

The purpose of generating the wide table is to upload it AWS Glue and hit into
the use case of dealing with _UnprocessedKeys_ when trying  to obtain the partitions.

https://docs.aws.amazon.com/cli/latest/reference/glue/batch-get-partition.html


It is initially not that obvious why AWS Glue API response for `batch-get-partition`
contains the field _UnprocessedKeys_ :

> A list of the partition values in the request for which partitions were not returned.


When actually calling for `1000` partitions which each have information about `3000` columns,
the payload to be delivered is really big. This is probably why the AWS Glue API developers
did opt for introducing the _UnprocessedKeys_ field. It is somehow a soft way of saying that
the request involves delivering a response payload too big. By using this rather unusual method
the response can be batched by forcing the client to do multiple smaller batch partition calls.
