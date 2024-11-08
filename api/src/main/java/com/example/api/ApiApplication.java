package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode; // Importar ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode; // Importar ObjectNode
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/java")
class ApiController {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ApiController() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

   @GetMapping
    public ResponseEntity<JsonNode> getApiData() {
        String firstUrl = "https://apipyton-0805.onrender.com/api/python"; // URL de la API de Python
        String secondUrl = "https://railway-production-ab84.up.railway.app/Railway"; // Segunda URL que devuelve un archivo txt
        
        try {
            // Llamada a la primera API (Python API)
            ResponseEntity<String> response1 = restTemplate.getForEntity(firstUrl, String.class);
            JsonNode data1 = processJsonResponse(response1);

            // Llamada a la segunda API (archivo de texto)
            ResponseEntity<String> response2 = restTemplate.getForEntity(secondUrl, String.class);
            String data2 = processTextResponse(response2);

            // Combina ambos resultados en un objeto JSON
            ObjectNode combinedData = objectMapper.createObjectNode();
            combinedData.put("JavaDice", "hola desde VERCEL");
            combinedData.set("jsonResponse", data1);
            combinedData.put("textResponse", data2); // Agrega el texto de la segunda API
            return ResponseEntity.ok(combinedData);

        } catch (Exception e) {
            // Agregar log para el error interno
            e.printStackTrace();  // Imprime el stack trace en los logs
            return ResponseEntity.status(500).body(objectMapper.createObjectNode().put("error", "Error interno en el servidor"));
        }
    }

    // Método para procesar la respuesta JSON
    private JsonNode processJsonResponse(ResponseEntity<String> response) throws Exception {
        return objectMapper.readTree(response.getBody());
    }

    // Método para procesar la respuesta de texto
    private String processTextResponse(ResponseEntity<String> response) {
        return response.getBody(); // Devuelve directamente el texto plano
    }
}
