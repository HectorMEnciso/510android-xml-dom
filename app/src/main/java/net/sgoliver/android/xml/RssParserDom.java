package net.sgoliver.android.xml;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RssParserDom
{
    Context context;
    DBController controller = new DBController(context);
	private URL rssUrl;
    HashMap<String, String> queryValues =  new  HashMap<String, String>();
	
	public RssParserDom(String url)
	{
		try 
		{
            this.rssUrl = new URL(url);
        } 
		catch (MalformedURLException e) 
		{
            throw new RuntimeException(e);
        }
	}

    public List<Noticia> parse() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Noticia> noticias = new ArrayList<Noticia>();
        
        try 
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(this.getInputStream());
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("item");
            
            for (int i=0; i<items.getLength(); i++)
            {
                Noticia noticia = new Noticia();
                
                Node item = items.item(i);
                NodeList datosNoticia = item.getChildNodes();
                
                for (int j=0; j<datosNoticia.getLength(); j++)
                {
                    Node dato = datosNoticia.item(j);
                    String etiqueta = dato.getNodeName();
                    
                    if (etiqueta.equals("title"))
                    {
                    	String texto = obtenerTexto(dato);
                    	
                    	noticia.setTitulo(texto);
                        queryValues.put("title", noticia.getTitulo());
                        Log.e("texto", texto.toString());
                        Log.e("titulo", noticia.getTitulo().toString());
                    } 
                    else if (etiqueta.equals("link"))
                    {
                    	noticia.setLink(dato.getFirstChild().getNodeValue());

                        queryValues.put("link", noticia.getLink());
                    } 
                    else if (etiqueta.equals("description"))
                    {
                        String texto = obtenerTexto(dato);
                        
                        noticia.setDescripcion(texto);

                        queryValues.put("description", noticia.getDescripcion());
                    } 
                    else if (etiqueta.equals("guid"))
                    {
                    	noticia.setGuid(dato.getFirstChild().getNodeValue());
                        queryValues.put("guid",noticia.getGuid());
                    }
                    else if (etiqueta.equals("pubDate"))
                    {
                    	noticia.setpubDate(dato.getFirstChild().getNodeValue());
                        queryValues.put("pubDate", noticia.getpubDate());
                    }

                }

                noticias.add(noticia);
//                controller.insertNoticia(queryValues);
            }
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException(ex);
        } 
        
        return noticias;
    }

	private String obtenerTexto(Node dato)
	{
		StringBuilder texto = new StringBuilder();
		NodeList fragmentos = dato.getChildNodes();
		
		for (int k=0;k<fragmentos.getLength();k++)
		{
		    texto.append(fragmentos.item(k).getNodeValue());
		}
		
		return texto.toString();
	}
    
	private InputStream getInputStream() 
	{
        try 
        {
            return rssUrl.openConnection().getInputStream();
        } 
        catch (IOException e) 
        {
            throw new RuntimeException(e);
        }
    }
}
