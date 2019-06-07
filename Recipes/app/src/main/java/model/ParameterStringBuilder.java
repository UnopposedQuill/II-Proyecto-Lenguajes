package model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Esta clase es importante por su método estático que permite colocar parámetros de forma sencilla
 */
public class ParameterStringBuilder {

    /**
     * Toma un Map y lo formatea de la manera en la que un servicio Http esperaría
     * @param params El Map que contiene los parámetros
     * @return Un String que representa el map, de manera que se pueda usar rápidamente para peticiones
     * http
     * @throws UnsupportedEncodingException
     */
    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
