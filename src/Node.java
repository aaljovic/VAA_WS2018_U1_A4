import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;


public class Node
{
    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            Node node = read(args[0]);
            //node.setNeighbours(readForNeighbours(Integer.toString(node.id)));
            //node.showNeighbours();
            //node.listenToPort(node.getPort());
        }
        else
        {
            System.out.println("Ungültige Eingabe." + "\n" + "Starten Sie das Programm neu mit der gewünschten Knoten ID.");
        }
    }

    private int id;
    private String ipAddress;
    private int port;
    private int[] neighbourNodes;

    public Node(int id, String ipAddress, int port, int[] neighbourNodes)
    {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.neighbourNodes = neighbourNodes;
    }

    protected static int[] readForNeighbours(String inputParameter)
    {
        String line = "";
        String idInLine = "LEER";
        String lastWordInLine = "";
        String lastIdInLine = "";
        int[] neighbours = new int[Constants.LENGTH_NODE_ARRAY];
        int numberOfNeighbours = 0;

        try {
            FileReader fr = new FileReader(Constants.GRAPH_FILE_NAME);
            BufferedReader br = new BufferedReader(fr);

            while (((line = br.readLine()) != null) && (!line.equals("}")))
            {
                idInLine = line.substring(0, line.indexOf(" "));
                lastWordInLine = line.substring(line.lastIndexOf(" ")+1);
                lastIdInLine = lastWordInLine.substring(0, (lastWordInLine.length()-1));
                if (idInLine.equals(inputParameter))
                {
                    neighbours[numberOfNeighbours] = Integer.parseInt(lastIdInLine);
                    numberOfNeighbours++;
                }
                else if (lastIdInLine.equals(inputParameter))
                {
                    neighbours[numberOfNeighbours] = Integer.parseInt(idInLine);
                    numberOfNeighbours++;
                }
            }
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println(Constants.FILE_NOT_FOUND_ERROR + fnfe);
        }
        catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        }
        catch (NullPointerException npe)
        {
            System.err.println("Die Eingegebene Nachbar-ID existiert nicht. Nullpointer Exception: " + npe);
        }

        int[] compactNeighbours = new int[numberOfNeighbours];
        for (int j=0; j<numberOfNeighbours; j++)
        {
            compactNeighbours[j] = neighbours[j];
        }

        return compactNeighbours;
    }

    protected static Node read(String inputParameter)
    {
        String line = "";
        String idInLine = "LEER";
        String ipAddress = "";

        try {
            FileReader fr = new FileReader(Constants.TEXT_FILE_NAME);
            BufferedReader br = new BufferedReader(fr);

            //Search in the File for the matching line (ID) with the user's input.
            while ((!idInLine.equals(inputParameter)) && ((line = br.readLine()) != null))
            {
                idInLine = line.substring(0, line.indexOf(" "));
            }
            ipAddress = line.substring(line.indexOf(" "));
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println(Constants.FILE_NOT_FOUND_ERROR + fnfe);
        }
        catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        }
        catch (NullPointerException npe)
        {
            System.err.println("Die Eingegebene ID existiert nicht. Nullpointer Exception: " + npe);
        }
        String[] parts = ipAddress.split(":");
        Node node = new Node(Integer.parseInt(idInLine), parts[0], Integer.parseInt(parts[1]), null);
        System.out.println("----> Knoten " + node.id + node.ipAddress + ":" + node.port + " <----");
        return node;
    }

    public static int[] getAllIds()
    {
        String line = "";
        String idInLine = "";
        int[] idAllLines = new int[Constants.LENGTH_NODE_ARRAY];
        int numberOfId = 0;

        try {
            FileReader fr = new FileReader(Constants.TEXT_FILE_NAME);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null)
            {
                idInLine = line.substring(0, line.indexOf(" "));
                idAllLines[numberOfId] = Integer.parseInt(idInLine);
                numberOfId++;
            }
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println(Constants.FILE_NOT_FOUND_ERROR + fnfe);
        }
        catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        }
        int[] idOfAllNodes = Arrays.copyOfRange(idAllLines, 0, numberOfId);
        return idOfAllNodes;
    }

    protected int getPort()
    {
        return this.port;
    }

    protected void setNeighbours(int[] neighbours)
    {
        this.neighbourNodes = neighbours;
    }

    protected String getIpAddress() { return this.ipAddress; }

    protected void listenToPort(int port)
    {
        ServerSocket server = null;
        boolean firstTime = true;
        boolean run = true;
        try
        {
            server = new ServerSocket(port);
            while (run==true)
            {
                System.out.println("\n" + "Server hört zu...");
                Socket socket = server.accept();

                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
                System.out.println(message);
                // String message includes a timestamp and the actual message. To check if the message contains the command "stop" we split it into two parts: timeStamp and actual message
                String[] wordsOfMessage = message.split("\\s+");
                String lastWordOfMessage = wordsOfMessage[wordsOfMessage.length-1];
                if (lastWordOfMessage.equals(Constants.STOP_MESSAGE))
                {
                    if (server != null)
                    {
                        server.close();
                        socket.close();
                        run = false;
                    }
                }
                else if (firstTime == true)
                {
                    this.sendIdToNeighbours();
                    firstTime = false;
                }
            }
        } catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        } finally
        {
            try
            {
                if (server != null)
                {
                    server.close();
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    protected static void sendMessage(int id, String message)
    {
        try
        {   // @TODO Maybe InetAddress.getLocalHost() instead of "127.0.0.1", or at least Constan localhost
            Socket clientSocket = new Socket("127.0.0.1", read(Integer.toString(id)).getPort());
            OutputStream os = clientSocket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
            message = timeStamp + "\t" + message;
            bw.write(message);
            bw.flush();
            clientSocket.close();
        }
        catch(UnknownHostException uhe)
        {
            System.err.println("Host ist unbekannt: " + uhe);
        }
        catch (SocketException sc)
        {
            System.err.println("Knoten ist bereits geschlossen: " + sc);
        }
        catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        }
    }

    protected void sendIdToNeighbours()
    {
        for (int i= 0; i<this.neighbourNodes.length; i++)
        {
            System.out.println("Nachricht an " + this.neighbourNodes[i]);
            this.sendMessage(this.neighbourNodes[i], Integer.toString(this.id));
        }

    }

    protected void setRandomNeighbours()
    {
        String line = "";
        String idInLine = "";
        int[] idAllLines = new int[Constants.LENGTH_NODE_ARRAY];
        int[] randomNeighbours = new int[3];
        int numberOfId = 0;
        int randomIndex = 0;
        Random generator = new Random();
        List<Integer> assignedNodes = new ArrayList<>();

        try {
            FileReader fr = new FileReader(Constants.TEXT_FILE_NAME);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null)
            {
                idInLine = line.substring(0, line.indexOf(" "));
                idAllLines[numberOfId] = Integer.parseInt(idInLine);
                numberOfId++;
            }
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println(Constants.FILE_NOT_FOUND_ERROR + fnfe);
        }
        catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        }
        int[] existingIdArray = Arrays.copyOfRange(idAllLines, 0, numberOfId);
        assignedNodes.add(this.id);
        for (int j=0; j<3; j++)
        {
            // A random number between 1 and the length of the Array is saved into the variable randomIndex
            randomIndex = generator.nextInt(existingIdArray.length);

            if (assignedNodes.contains(existingIdArray[randomIndex]))
            {
                j--;
            }
            else
            {
                randomNeighbours[j] = existingIdArray[randomIndex];
                assignedNodes.add(existingIdArray[randomIndex]);
            }
        }
        this.neighbourNodes = randomNeighbours;
    }

    protected void showNeighbours()
    {
        for (int i=0; i<this.neighbourNodes.length; i++)
        {
            System.out.println("Nachbar " + this.neighbourNodes[i]);
        }
    }
}