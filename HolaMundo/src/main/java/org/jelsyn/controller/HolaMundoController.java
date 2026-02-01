package org.jelsyn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HolaMundoController {

    @GetMapping("/hola")
    public Map<String, String> holaMundo() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "¡Hola Mundo!");

        try {
            // Obtener la dirección IP del servidor
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String hostName = inetAddress.getHostName();

            response.put("ip", ipAddress);
            response.put("hostname", hostName);

            // En AWS, también podemos intentar obtener la IP privada
            String privateIp = System.getenv("HOSTNAME");
            if (privateIp != null) {
                response.put("container_hostname", privateIp);
            }

        } catch (UnknownHostException e) {
            response.put("ip", "No se pudo obtener la IP");
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return response;
    }
}

