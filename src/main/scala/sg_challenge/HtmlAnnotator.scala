package sg_challenge

import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Node, TextNode}
import org.jsoup.parser.{Parser, Tag}

import scala.collection.JavaConverters._

object HtmlAnnotator extends Annotator {
  val NameRE = "[A-Za-z0-9]+".r
  val Anchor = Tag.valueOf("a")

  override def annotate(annotations: Map[String, String], data: String): String = {
    val root = Jsoup.parse(data, "", Parser.xmlParser())
    root.outputSettings().prettyPrint(false)
    for {
      node <- unanchoredTextNodes(root)
      m <- NameRE.findAllMatchIn(node.getWholeText).toSeq.reverseIterator
      url <- annotations.get(m.matched)
    } {
      if (m.end < node.getWholeText.length)
        node.splitText(m.end)
      anchor(node.splitText(m.start), url)
    }
    root.outerHtml()
  }

  def unanchoredTextNodes(root: Node): Seq[TextNode] = {
    lazy val loop: Node => Stream[TextNode] = {
      case node: TextNode => Stream(node)
      case node if node.nodeName() == Anchor.getName => Stream.empty
      case node => node.childNodes().asScala.toStream.flatMap(loop)
    }
    loop(root).force
  }

  def anchor(node: Node, url: String): Unit = {
    require(node.parent() != null, "node.parent() != null")
    val newNode = new Element(Anchor, "").attr("href", url)
    node.replaceWith(newNode)
    newNode.appendChild(node)
  }
}
