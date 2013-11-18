
package edu.mbl.jif.imaging.meta;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

/**
 * Helper class for debugging stuff in Image I/O.
 *
 * @version $Id: ImageIODebugUtil.java 610706 2008-01-10 08:14:18Z jeremias $
 */
public class ImageIODebugUtil {

    public static void dumpMetadata(IIOMetadata meta, boolean nativeFormat) {
        String format;
        if (nativeFormat) {
            format = meta.getNativeMetadataFormatName();
        } else {
            format = IIOMetadataFormatImpl.standardMetadataFormatName;
        }
        Node node = meta.getAsTree(format);
        dumpNode(node);
    }
    
    public static void dumpNode(Node node) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            Source src = new DOMSource(node);
            Result res = new StreamResult(System.out);
            t.transform(src, res);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
