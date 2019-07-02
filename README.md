# Annotation Service

To work with the application you will need `sbt` and JDK 1.8 installed (I think it should work on 1.7, but I didn't test).

Run service from console using `sbt`:

    user@user:~/sg_challenge$ sbt run

Web API:

`PUT` `/names/[name]`
  * storage uses `ConcurrentHashMap`, which has space and time complexity - O(1) for basic operations;

`GET` `/names/[name]`
  * O(1) (see above);

`DELETE` `/names`

  * `ConcurrentHashMap` implementation (to achieve concurrency) traverses internal structure of the map, so complexity is O(entities count), and space is O(1);
  * this approach assumes deletion will be rare operation;

`POST` `/annotate` algorithm is straightforward:
  1. parse input: time O(N), space O(N)
  2. look for non anchored text nodes in parsed DOM: time O(N); space O(N) due to search result materialization
  3. look for names in text node text: time O(N); space O(1)
  4. filter names which have corresponding URLs in storage (complexity depends on storage, thus time and space: O(1))
  5. split entire text node into smaller text nodes in place of filtered names: time O(N); space O(N)
  6. anchor text nodes corresponding to names to annotate: time O(1); space O(1)

Thus complexity of the algorithm is linear by time and space.
