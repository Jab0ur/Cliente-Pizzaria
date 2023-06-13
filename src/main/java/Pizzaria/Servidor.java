package Pizzaria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    private static final Map<String, String> MENU = new HashMap<>();
    private static final Map<String, String> BORDAS = new HashMap<>();
    private static final Map<String, String> BEBIDAS = new HashMap<>();

    static {
        // Preencha o mapa MENU com os sabores de pizzas disponíveis
        MENU.put("1", "Pizza de Calabresa");
        MENU.put("2", "Pizza de Mussarela");
        MENU.put("3", "Pizza de Frango");
        MENU.put("4", "Pizza de Margherita");
        MENU.put("5", "Pizza de Pepperoni");
        MENU.put("6", "Pizza de Portuguesa");
        MENU.put("7", "Pizza de Quatro Queijos");
        MENU.put("8", "Pizza de Bacon");
        MENU.put("9", "Pizza de Vegetariana");
        MENU.put("10", "Pizza de Frango com Catupiry");
        MENU.put("11", "Pizza de Atum");
        MENU.put("12", "Pizza de Rúcula com Tomate Seco");
        MENU.put("13", "Pizza de Calabresa com Cebola");
        MENU.put("14", "Pizza de Palmito");
        MENU.put("15", "Pizza de Chocolate");

        // Preencha o mapa BORDAS com as opções de bordas disponíveis
        BORDAS.put("1", "Borda Doce");
        BORDAS.put("2", "Borda Cheddar");
        BORDAS.put("3", "Borda Catupiry");
        BORDAS.put("4", "Sem Borda");

        // Preencha o mapa BEBIDAS com as opções de bebidas disponíveis
        BEBIDAS.put("1", "Suco de Laranja");
        BEBIDAS.put("2", "Suco de Limão");
        BEBIDAS.put("3", "Suco de Morango");
        BEBIDAS.put("4", "Suco de Abacaxi");
        BEBIDAS.put("5", "Suco de Uva");
        BEBIDAS.put("6", "Refrigerante Coca-Cola");
        BEBIDAS.put("7", "Refrigerante Pepsi");
        BEBIDAS.put("8", "Refrigerante Guaraná");
        BEBIDAS.put("9", "Refrigerante Sprite");
        BEBIDAS.put("10", "Refrigerante Fanta");
    }

    public static void main(String[] args) {
        try {
            int serverPort = 8888; // Porta do servidor

            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Servidor iniciado. Aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                Thread clientHandlerThread = new Thread(() -> {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                        // Exibe a mensagem de boas-vindas e solicita ao cliente para iniciar um pedido
                        writer.println("Servidor: Bem-vindo à Pizzaria! Para iniciar um pedido, digite 'PEDIR'");
                        writer.println();

                        String request = reader.readLine();
                        System.out.println("Requisição do cliente: " + request);

                        // Verifica a requisição do cliente
                        if (request.equals("PEDIR")) {
                            sendMenu(writer);
                            String response = reader.readLine();
                            System.out.println("Resposta do cliente (Sabores): " + response);
                            processSabores(response, writer);

                            sendOpcoesBordas(writer);
                            response = reader.readLine();
                            System.out.println("Resposta do cliente (Bordas): " + response);
                            processBordas(response, writer);

                            sendBebidas(writer);
                            response = reader.readLine();
                            System.out.println("Resposta do cliente (Bebidas): " + response);
                            processBebidas(response, writer);

                            writer.println("Pedido finalizado. Obrigado!");
                        } else {
                            writer.println("Servidor: Requisição inválida. Encerrando conexão.");
                        }

                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                clientHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMenu(PrintWriter writer) {
        // Constrói a mensagem do cardápio completo com os sabores de pizzas disponíveis
        StringBuilder menuMessage = new StringBuilder("Servidor: Cardápio completo: ");
        for (Map.Entry<String, String> entry : MENU.entrySet()) {
            menuMessage.append(entry.getKey()).append(". ").append(entry.getValue()).append(" | ");
        }
        menuMessage.setLength(menuMessage.length() - 3); // Remove o último caractere "|"
        writer.println(menuMessage.toString());
        writer.flush();
    }

    private static void sendOpcoesBordas(PrintWriter writer) {
        // Constrói a mensagem das opções de bordas disponíveis
        StringBuilder bordasMessage = new StringBuilder("Servidor: Opções de Bordas: ");
        for (Map.Entry<String, String> entry : BORDAS.entrySet()) {
            bordasMessage.append(entry.getKey()).append(". ").append(entry.getValue()).append(" | ");
        }
        bordasMessage.setLength(bordasMessage.length() - 3); // Remove o último caractere "|"
        writer.println(bordasMessage.toString());
        writer.flush();
    }

    private static void sendBebidas(PrintWriter writer) {
        // Constrói a mensagem das opções de bebidas disponíveis
        StringBuilder bebidasMessage = new StringBuilder("Servidor: Opções de Bebidas: ");
        for (Map.Entry<String, String> entry : BEBIDAS.entrySet()) {
            bebidasMessage.append(entry.getKey()).append(". ").append(entry.getValue()).append(" | ");
        }
        bebidasMessage.setLength(bebidasMessage.length() - 3); // Remove o último caractere "|"
        writer.println(bebidasMessage.toString());
        writer.flush();
    }

    private static void processSabores(String response, PrintWriter writer) {
        String[] sabores = response.split(",");
        writer.println("Sabores selecionados: ");
        for (String sabor : sabores) {
            String pizza = MENU.getOrDefault(sabor, "");
            writer.println(sabor + ". " + pizza);
        }
        writer.println();
        writer.flush();
    }

    private static void processBordas(String response, PrintWriter writer) {
        String borda = BORDAS.getOrDefault(response, "");
        writer.println("Borda selecionada: " + borda);
        writer.println();
        writer.flush();
    }

    private static void processBebidas(String response, PrintWriter writer) {
        String bebida = BEBIDAS.getOrDefault(response, "");
        writer.println("Bebida selecionada: " + bebida);
        writer.println();
        writer.flush();
    }
}