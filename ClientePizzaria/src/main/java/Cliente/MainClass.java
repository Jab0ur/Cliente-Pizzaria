package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainClass extends JFrame {
    private JTextArea outputTextArea;
    private JTextField inputTextField;
    private JButton sendButton;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public MainClass() {
        setTitle("Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        setLayout(new BorderLayout());

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputTextField = new JTextField();
        inputPanel.add(inputTextField, BorderLayout.CENTER);

        sendButton = new JButton("Enviar");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.setEnabled(false);
                inputTextField.setEnabled(false);
                String request = inputTextField.getText();
                processRequest(request);
                inputTextField.setText("");
                sendButton.setEnabled(true);
                inputTextField.setEnabled(true);
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        connectToServer();
        startReading();
    }

    private void connectToServer() {
        try {
            String serverIP = "127.0.0.1"; // IP do servidor
            int serverPort = 8888; // Porta do servidor

            socket = new Socket(serverIP, serverPort);
            System.out.println("Conectado ao servidor: " + serverIP + ":" + serverPort);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReading() {
        Thread readingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        outputTextArea.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readingThread.start();
    }

    private void processRequest(String request) {
        outputTextArea.append("Cliente: " + request + "\n");
        writer.println(request);
        writer.flush();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainClass frame = new MainClass();
                frame.setVisible(true);
            }
        });
    }
}
