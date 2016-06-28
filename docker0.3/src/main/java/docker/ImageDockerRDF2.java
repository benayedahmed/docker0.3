package docker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.io.FileWriter;
import java.io.IOException;

public class ImageDockerRDF2 {
	static String imageName;
	static String lastPushed;
	static String imageShortDescription;
	static String dockerPullCommand;
	static String imageVesion;
	static String imageLinkGit;
	static String dockerRunCommand;
	static String linkWikipedia;
	static String linkDBpedia;
	static String dockerVersionSupported;
	static Document doc;

	public static void main(String[] args) throws Throwable {
		Elements newsHeadlines;
		Element newsHeadline;
		HttpResponse<JsonNode> response;
		int i = 0;
		String url;

		/*for (i = 1; i <= 3; i++) {
			doc = Jsoup.connect("https://hub.docker.com/explore/?page=" + i).get();
			newsHeadlines = doc.select(".RepositoryListItem__flexible___3R0Sg");
			for (Element element : newsHeadlines) {
				String attr = element.select("a").first().attr("href");
				url = "https://hub.docker.com" + attr;
		 */
				String rdf = "";
				url = "https://hub.docker.com/_/wordpress/";
				doc = Jsoup.connect(url).get();

				// 1. image name
				newsHeadline = doc.select("h2.RepositoryPageWrapper__repoTitle___3r12T a").get(0);
				imageName = newsHeadline.text();

				// ********** block column headers line CSV file ***** //

				String sFileName = "C:/csv/" + imageName + ".csv";
				FileWriter writer = new FileWriter(sFileName);
				writer.append("container:imageName");
				writer.append(',');
				writer.append("container:lastPushed");
				writer.append(',');
				writer.append("container:imageShortDescription");
				writer.append(',');
				writer.append("container:dockerPullCommand");
				writer.append(',');
				writer.append("container:dockerRunCommand");
				writer.append(',');
				writer.append("prov:specializationOf");
				writer.append(',');
				writer.append("container:dockerVersionSupported");
				writer.append(',');

				//writer.append("container:ImageVersions");
				//writer.append('[');
				// 0,* elements
				//writer.append("container:ImageVersion");
				writer.append('[');
				writer.append("container:ImageVersionCode");
				writer.append(',');
				writer.append("container:ImageLinkGit");
				writer.append(']');
				// *****
				//writer.append(']');
				writer.append(',');
				writer.append("container:LinkedDockerImages");
				writer.append(',');

				//writer.append("comments");
				//writer.append('[');
				// 0,* elements
				//writer.append("comment");
				writer.append('[');
				writer.append("sioc:name");
				writer.append(',');
				writer.append("container:commentDate");
				writer.append(',');
				writer.append("sioc:Content");
				writer.append(']');
				// *****
				//writer.append(']');
				writer.append('\n');

				// ******************* end block ********//

				// 2. last pushed
				newsHeadline = doc.select("span.RepositoryPageWrapper__repoSubtitle___34EVq span").get(1);
				lastPushed = newsHeadline.text();

				// 3. short description
				newsHeadline = doc.select("div.large-8 div.Card__block___1G9Iy span").get(1);
				imageShortDescription = newsHeadline.text();

				// 4. docker pull command
				newsHeadline = doc
						.select("div.Card__card___1LRg9 div.Card__block___1G9Iy div.PullCommand__pullCommand___3N0iQ input")
						.get(0);
				dockerPullCommand = newsHeadline.val();

				// 5. docker run command
				newsHeadline = doc.select("code:contains(docker run)").get(0);
				dockerRunCommand = newsHeadline.text();

				// 6. Wikipedia link
				newsHeadline = doc.select("a[href*=wikipedia]").get(0);
				linkWikipedia = newsHeadline.text().toString();

				// 7. docker version supported
				newsHeadline = doc.select("p:contains(docker version)").get(0);
				dockerVersionSupported = newsHeadline.text();

				// System.out.print(dockerVersionSupported);

				rdf = "<?xml version=\"1.0\"?>\n" + "<!DOCTYPE rdf:RDF ["
						+ "				<!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\n"
						+ "				<!ENTITY nerd \"http://nerd.eurecom.fr/ontology#\" >\n"
						+ "				<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\n"
						+ "				<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\n"
						+ "				<!ENTITY jrcn \"http://mlode.nlp2rdf.org/jrc-names/\" >\n"
						+ "				<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\n"
						+ "			]>\n" + "			<rdf:RDF xmlns=\"http://www.w3.org/2002/07/owl#\"\n"
						+ "				 xml:base=\"http://www.w3.org/2002/07/owl\"\n"
						+ "				 xmlns:nerd=\"http://nerd.eurecom.fr/ontology#\"\n"
						+ "				 xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n"
						+ "				 xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n"
						+ "				 xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\n"
						+ "				 xmlns:jrcn=\"http://mlode.nlp2rdf.org/jrc-names/\"\n"
						// *** Used ***
						+ "				 xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
						+ "				 xmlns:foaf=\"http://xmlns.com/foaf/0.1/#\"\n"
						+ "				 xmlns:sioc=\" http://rdfs.org/sioc/ns#\"\n"
						+ "				 xmlns:dbo=\"http://dbpedia.org/ontology/#\"\n"
						+ "				 xmlns:container=\"http://TSE.com/container/#\">\n"; // define
																								// container
				writer.append(',');
				writer.append('\n');
				rdf += "<rdf:Description rdf:about=\"" + url + "\">\n";
				// rdf += "<container:seeAlso>" + linkWikipedia +
				// "</container:seeAlso>\n";
				rdf += "<container:imageName>" + imageName + "</container:imageName>\n";
				writer.append(imageName);
				writer.append(',');
				rdf += "<container:lastPushed>" + lastPushed + "</container:lastPushed>\n";
				writer.append(lastPushed);
				writer.append(',');
				rdf += "<container:imageShortDescription>" + imageShortDescription
						+ "</container:imageShortDescription>\n";
				writer.append(imageShortDescription);
				writer.append(',');
				rdf += "<container:dockerPullCommand>" + dockerPullCommand + "</container:dockerPullCommand>\n";
				writer.append(dockerPullCommand);
				writer.append(',');
				rdf += "<container:dockerRunCommand>" + dockerRunCommand + "</container:dockerRunCommand>\n";
				writer.append(dockerRunCommand);
				writer.append(',');
				rdf += "<prov:specializationOf>" + linkWikipedia + "</prov:specializationOf>\n";
				writer.append(linkWikipedia);
				writer.append(',');
				linkDBpedia="http://fr.dbpedia.org/page/"+imageName;
				rdf += "<dbpedia-owl:wikiPageRedirects>" + linkDBpedia + "</dbpedia-owl:wikiPageRedirects>\n";
				writer.append(linkDBpedia);
				writer.append(',');
				rdf += "<container:dockerVersionSupported>" + dockerVersionSupported
						+ "</container:dockerVersionSupported>\n";
				writer.append(dockerVersionSupported);
				writer.append(',');
				// 8. image version
				// 9. link image
				newsHeadlines = doc.select(
						"div.Card__block___1G9Iy div.Markdown__markdown___2_Qaz li a[href*=https://github.com/] ");

				org.jsoup.nodes.Attributes att;
				Elements eCodes;

				//rdf += "<container:ImageVersions>\n";
				//writer.append("[");
				for (Element langelement : newsHeadlines) {
					att = langelement.select("a[href*=https://github.com/]").get(0).attributes();

					eCodes = langelement.select("code");
					i = 0;
					for (Element eCode : eCodes) {
						i++;
						rdf += "<container:ImageVersion>\n";
						writer.append("[");
						rdf += "<container:ImageVersionCode>" + eCode.text() + "</container:ImageVersionCode>\n";
						writer.append(eCode.text());
						writer.append(",");
						rdf += "<container:ImageLinkGit>" + att.toString() + "</container:ImageLinkGit>\n";
						writer.append(att.toString());

						rdf += "</container:ImageVersion>\n";
						writer.append("]");
						i++;
						if (i != (eCodes.size() - 1))
							writer.append(",");
					}
					//rdf += "</container:ImageVersions>\n";
					//writer.append("]");
					//writer.append(",");
				}

				// --------------- Block linked docker images

				newsHeadlines = doc.select("code:contains(--link)");
				String[] splitArray1 = null; // tableau de chaînes
				String[] splitArray2 = null;
				ArrayList list = new ArrayList();
				Set set = new HashSet();
				ArrayList distinctList;
				String str = newsHeadlines.text();
				String imgaux = null;
				splitArray1 = str.split(" ");
				for (i = 0; i < splitArray1.length; i++) {
					if (splitArray1[i].equals("--link") && splitArray1[i + 1].indexOf(':') != -1)
						imgaux = splitArray1[i + 1];
					if (imgaux != null) {
						splitArray2 = imgaux.split(":");
						if (!list.contains(splitArray2[1])) {
							list.add(splitArray2[1]);
						}

					}

				}
				set.addAll(list);
				distinctList = new ArrayList(set);
				//rdf += "<container:LinkedDockerImages>\n";
				//writer.append("[");
				for (i = 0; i < distinctList.size(); i++) {
					rdf += "<container:LinkedDockerImage>" + distinctList.get(i) + "</container:LinkedDockerImage>\n";
					writer.append(distinctList.get(i).toString());
					if (i != (distinctList.size() - 1))
						writer.append(",");
				}
				//rdf += "</container:LinkedDockerImages>\n";
				//writer.append("]");
				writer.append(",");
				// --------------------------------

				// 10. Comment
				// 11. name Comment
				// 12. date Comment
				newsHeadlines = doc.select("div.columns div.large-12 div.Card__card___1LRg9");// div.Markdown__markdown___2_Qaz
				Elements commentName, commentDate, commentContent;
				i = 0;
				//rdf += "<sioc:comments>\n";
				//writer.append("[");
				for (Element newsHeadline1 : newsHeadlines) {
					//rdf += "<sioc:comment>\n";
					writer.append("[");
					commentName = newsHeadline1.select("div.Comment__name___3ImyG");
					rdf += "<sioc:Name>" + commentName.text() + "</sioc:Name>\n";
					writer.append(commentName.text());
					writer.append(",");
					commentDate = newsHeadline1.select("div.Comment__time___1wuRD");
					rdf += "<container:commentDate>" + commentDate.text() + "</container:commentDate>\n";
					writer.append(commentDate.text());
					writer.append(",");
					commentContent = newsHeadline1.select("div.Markdown__markdown___2_Qaz");
					rdf += "<sioc:Content>" + commentContent.text() + "</sioc:Content>\n";
					writer.append(commentContent.text().replaceAll("\n", ""));

					/*
					 * 13. comment opinion response =
					 * Unirest.post("http://sentiment.vivekn.com/api/text/")
					 * 
					 * .field("txt", commentContent.text()).asJson(); str =
					 * response.getBody().getObject().get("result").toString();
					 * JSONObject obj = new JSONObject(str); rdf +=
					 * "<sioc:commentOpinion>" + obj.getString("sentiment") +
					 * "</rdfs:commentOpinion>\n"; rdf +=
					 * "<sioc:commentOpinionConfidence>" +
					 * obj.getString("confidence") +
					 * "</rdfs:commentOpinionConfidence>\n";
					 */
					//rdf += "</sios:comment>\n";
					writer.append("]");
					if (i != (newsHeadlines.size() - 1))
						writer.append(",");

					i++;
				}
				//rdf += "</sioc:comments>\n";
				//writer.append("]");

				rdf += "</rdf:Description>\n";
				rdf += "</rdf:RDF>";
				System.out.println(rdf);
				writer.flush();
				writer.close();

			}

	//	}// For (all images)
	//} // For (all images)

}
