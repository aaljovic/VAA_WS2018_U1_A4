import org.json.*;

import java.io.IOException;
import java.util.Scanner;

public class Initiator
{
    public static void main(String[] args)
    {
        int nodesAndEdges[] = checkInput();
        Graphgen.changeTextFile(nodesAndEdges[0]);
        int randomNumber = Graphgen.getRandomNumber(nodesAndEdges[0]);
        Graphgen[] allNeighbours = Graphgen.getNeighboursForGraph(randomNumber, nodesAndEdges[0], nodesAndEdges[1]);
        Graphgen.changeGraphFile(nodesAndEdges[1], allNeighbours);
        /*
        try
        {
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "startNodes.bat"});
        }
        catch(IOException io)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR);
        }
        */


        while (true)
        {
            System.out.print(Constants.SELECTION_MENU);
            Scanner sc = new Scanner(System.in);

            String input = sc.next();
            switch (input)
            {
                case "1":
                    tellASecretTo();
                    break;
                case "2":
                    closeNode();
                    break;
                case "3":
                    closeAllNodes();
                    break;
                default:
                    System.out.println("Eingabe unzutreffend");
            }
        }
    }

    private static int[] checkInput()
    {
        System.out.println("Geben Sie die Anzahl der Knoten ein: ");
        Scanner sc = new Scanner(System.in);
        int numberOfNodes = Integer.parseInt(sc.next());
        System.out.println("Geben Sie die Anzahl der Kanten ein: ");
        int numberOfEdges = Integer.parseInt(sc.next());

        if (numberOfEdges <= numberOfNodes)
        {
            System.out.println("Anzahl der Kanten muss größer sein als Anzahl der Knoten.");
            checkInput();
        }
        else if (numberOfEdges > ((numberOfNodes*(numberOfNodes-1))/2))
        {
            System.out.println("Anzahl der Kanten ist zu hoch. (Höher als  (n*(n-1))/2 )");
            checkInput();
        }
        else if (numberOfNodes < 3)
        {
            System.out.println("Anzahl der Knoten ist zu gering.");
            checkInput();
        }
        return new int[] {numberOfNodes, numberOfEdges};
    }

    private static String chooseNodeId()
    {
        System.out.println(Constants.REQUEST_ID_INPUT);
        Scanner sc = new Scanner(System.in);
        String input = sc.next();
        return input;
    }

    private static void tellASecretTo()
    {
        System.out.println(Constants.REQUEST_ID_INPUT);
        Scanner sc = new Scanner(System.in);
        String id = sc.next();
        System.out.println("Wie lautet die Nachricht?");
        String message = sc.next();
        //Node.sendMessage(Integer.parseInt(id), message);

        JSONObject wholeMessage = Node.createWholeMessage(Integer.parseInt(id), 0, "tellSecret", message);
        Node.sendSecret(wholeMessage);
    }

    private static void sendMessageTo()
    {
        System.out.println(Constants.REQUEST_ID_INPUT);
        Scanner sc = new Scanner(System.in);
        String id = sc.next();
        System.out.println("Wie lautet die Nachricht?");
        String message = sc.next();
        Node.sendMessage(Integer.parseInt(id), message);
    }

    private static void closeNode()
    {
        System.out.println(Constants.REQUEST_ID_INPUT);
        Scanner sc = new Scanner(System.in);
        String id = sc.next();
        //Node.sendMessage(Integer.parseInt(id), Constants.STOP_MESSAGE);
        JSONObject wholeMessage = Node.createWholeMessage(Integer.parseInt(id), 0, Constants.STOP_MESSAGE, "Knoten wurde beendet");
        Node.sendSecret(wholeMessage);
    }

    private static void closeAllNodes()
    {
        int[] allIDs = Node.getAllIds();
        for (int i=0; i< allIDs.length; i++)
        {
            //Node.sendMessage(allIDs[i], Constants.STOP_MESSAGE);
            JSONObject wholeMessage = Node.createWholeMessage(allIDs[i], 0, Constants.STOP_MESSAGE, "Knoten wurde beendet");
            Node.sendSecret(wholeMessage);
        }
    }
}
