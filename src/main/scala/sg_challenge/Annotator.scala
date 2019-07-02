package sg_challenge

trait Annotator {
  def annotate(annotations: Map[String, String], data: String): String
}
