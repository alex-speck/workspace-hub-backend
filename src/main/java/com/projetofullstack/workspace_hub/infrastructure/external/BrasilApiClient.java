package com.projetofullstack.workspace_hub.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetofullstack.workspace_hub.infrastructure.dto.BrasilApiResponse;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.BusinessException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class BrasilApiClient {

    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final ObjectMapper objectMapper = new ObjectMapper();



    public BrasilApiResponse buscarEmpresaPorCNPJ(String cnpj) {
        String cnpjNumeros = cnpj.replaceAll("[^0-9]", "");

        String url = "https://brasilapi.com.br/api/cnpj/v1/" + cnpjNumeros;

        try{
            System.out.println("Começado a consulta do cnpj: " + cnpjNumeros);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 404){
                System.out.println("Erro ao buscar empresa por CNPJ: " + cnpjNumeros);
                throw new BusinessException("Não existe empresa com esse CNPJ");
            }
            if(response.statusCode() == 200){
                System.out.println("Empresa encontrada: " + cnpjNumeros);
                return objectMapper.readValue(response.body(), BrasilApiResponse.class);
            }
            System.out.println("Erro ao buscar empresa por CNPJ: " + cnpjNumeros);
            throw new RuntimeException("Erro ao buscar empresa por CNPJ");
        } catch (BusinessException e){
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Falha na comunicação com a API do BrasilAPI: " + e.getMessage());
        }
    }

}
