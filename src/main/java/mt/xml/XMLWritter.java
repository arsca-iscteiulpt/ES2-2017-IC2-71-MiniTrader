package mt.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import mt.Order;

public class XMLWritter {

	private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder dBuilder;
	private Document doc;
	private Element eRegion;
	private String filePath = "file.xml";
	private Region region;

	public XMLWritter(Region region) {

		try {

			openFile(region);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addOrder(Order order) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);

			Node firstElement = doc.getElementsByTagName("region").item(0);

			Element eOrder = doc.createElement("order");
			eOrder.setAttribute("serverId", String.valueOf(order.getServerOrderID()));
			firstElement.appendChild(eOrder);

			Element stock = doc.createElement("stock");
			stock.appendChild(doc.createTextNode(order.getStock()));

			Element numberOfUnits = doc.createElement("numberOfUnits");
			numberOfUnits.appendChild(doc.createTextNode(String.valueOf(order.getNumberOfUnits())));

			Element pricePerUnit = doc.createElement("pricePerUnit");
			pricePerUnit.appendChild(doc.createTextNode(String.valueOf(order.getPricePerUnit())));

			Element isBuyOrder = doc.createElement("isBuyOrder");
			isBuyOrder.appendChild(doc.createTextNode(String.valueOf(order.isBuyOrder())));

			if (region != Region.EU) {

				eOrder.appendChild(stock);
				eOrder.appendChild(numberOfUnits);
				eOrder.appendChild(pricePerUnit);
				eOrder.appendChild(isBuyOrder);

			}

			if (region == Region.AS) {

				Element nickName = doc.createElement("nickName");
				nickName.appendChild(doc.createTextNode(order.getNickname()));

				eOrder.appendChild(nickName);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openFile(Region region) {
		try {

			this.region = region;
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.newDocument();

			eRegion = doc.createElement("region");
			eRegion.setAttribute("id", region.toString());
			doc.appendChild(eRegion);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));

			// Output to console for testing
			StreamResult sResult = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
