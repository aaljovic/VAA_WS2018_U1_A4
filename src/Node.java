import org.json.JSONException;
import org.json.JSONObject;

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
            node.setNeighbours(readForNeighbours(Integer.toString(node.id)));
            node.showNeighbours();
            node.listenToPort(node.getPort());
            System.out.println("Das Gerücht hörte ich " + node.heard + " Mal.");
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
    private int heard;

    public Node(int id, String ipAddress, int port, int[] neighbourNodes, int heard)
    {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.neighbourNodes = neighbourNodes;
        this.heard = heard;
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
                //lastIdInLine = lastWordInLine.substring(0, (lastWordInLine.length()-1));
                if (idInLine.equals(inputParameter))
                {
                    neighbours[numberOfNeighbours] = Integer.parseInt(lastWordInLine);
                    numberOfNeighbours++;
                }
                else if (lastWordInLine.equals(inputParameter))
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
        Node node = new Node(Integer.parseInt(idInLine), parts[0], Integer.parseInt(parts[1]), null, 0);
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

    protected void showPartOfMessage(JSONObject wholeMessage)
    {
        try
        {
            System.out.println(Constants.JSON_MESSAGE_START + wholeMessage.getString("timeStamp") + "\t" + wholeMessage.getString("secret") + "\tvon ID: " + wholeMessage.getInt("fromID"));
        }
        catch(JSONException jsonE)
        {
            System.err.println(Constants.JSON_GENERAL_ERROR + jsonE);
        }
    }

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
                JSONObject wholeMessage = new JSONObject(message);
                showPartOfMessage(wholeMessage);

                String lastWordOfMessage = wholeMessage.getString("controlMessage");
                if (lastWordOfMessage.equals(Constants.STOP_MESSAGE))
                {
                    if (server != null)
                    {
                        server.close();
                        socket.close();
                        run = false;
                        this.heard--;
                    }
                }
                else if (firstTime == true)
                {
                    //this.sendIdToNeighbours();
                    this.sendSecretToNeighbours(wholeMessage);
                    firstTime = false;
                }
                this.heard++;
            }

        } catch (JSONException jsonE)
        {
            System.err.println(Constants.JSON_GENERAL_ERROR + jsonE);
        }catch (IOException ioe)
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

    protected static void sendSecret(JSONObject secretMessage)
    {
        try
        {   // @TODO Maybe InetAddress.getLocalHost() instead of "127.0.0.1", or at least Constan localhost
                Socket clientSocket = new Socket("127.0.0.1", read(Integer.toString(secretMessage.getInt("toID"))).getPort());
                OutputStream os = clientSocket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(secretMessage.toString());
                bw.flush();
                clientSocket.close();
        }
        catch (JSONException jsonE)
        {
            System.err.println(Constants.JSON_GENERAL_ERROR + jsonE);
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

    protected void sendSecretToNeighbours(JSONObject secretMessage)
    {
        for (int i= 0; i<this.neighbourNodes.length; i++)
        {
            System.out.println("Nachricht an " + this.neighbourNodes[i]);
            try
            {
                secretMessage = this.createWholeMessage(this.neighbourNodes[i], this.id, secretMessage.getString("controlMessage"), secretMessage.getString("secret"));
            }
            catch(JSONException jsonE)
            {
                System.err.println(Constants.JSON_GENERAL_ERROR + jsonE);
            }
            this.sendSecret(secretMessage);
        }

    }

    protected static JSONObject createWholeMessage(int toID, int fromID, String controlMessage, String secret)
    {
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSSSSS").format(new Date());
        JSONObject wholeMessage = new JSONObject();

        try
        {
            wholeMessage.put("toID", toID);
            wholeMessage.put("fromID", fromID);
            wholeMessage.put("secret", secret);
            wholeMessage.put("controlMessage", controlMessage);
            wholeMessage.put("timeStamp", timeStamp);
        }
        catch(JSONException jsonE)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + jsonE);
        }
        return wholeMessage;
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