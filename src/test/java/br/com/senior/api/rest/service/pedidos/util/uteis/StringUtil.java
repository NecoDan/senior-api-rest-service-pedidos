package br.com.senior.api.rest.service.pedidos.util.uteis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class StringUtil {

    private StringUtil() {

    }

    public static String formatConteudoJSONFrom(String conteudo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(conteudo);
        return gson.toJson(je);
    }

    public static String formatConteudoXMLFrom(String conteudo) throws Exception {
        if (Objects.isNull(conteudo) || conteudo.isEmpty())
            return "";

        Source xmlInput = new StreamSource(new StringReader(conteudo));
        StringWriter out = new StringWriter();

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(xmlInput, new StreamResult(out));
        return out.toString().trim();
    }

    public static String formatLocalDate(LocalDate data) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data);
    }
}
