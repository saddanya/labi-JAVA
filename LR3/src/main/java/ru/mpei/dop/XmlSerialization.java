package ru.mpei.dop;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlSerialization {
	
	public static <T> void serialize(T inf, String outPathFile) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(inf.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(inf, new File(outPathFile));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static <T> T deserialize(Class<T> clas, String inPathFile){
	    T object=null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clas);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Object obj = jaxbUnmarshaller.unmarshal( new File(inPathFile));
			try {
				object = (T) obj;
			}
			catch (Exception e) {
				object = null;
				e.printStackTrace();
			}
		} catch (JAXBException ee ) {
			ee.printStackTrace();
		}
		return  object;
	}
}