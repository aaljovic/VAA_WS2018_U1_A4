import java.util.Scanner;

public class Initiator
{
    public static void main(String[] args)
    {
        int nodesAndEdges[] = checkInput();
        Graphgen.changeTextFile(nodesAndEdges[0]);
        //Graphgen neighbours = Graphgen.getNeighbourForGraph(Graphgen.getRandomNumber(nodesAndEdges[0]), nodesAndEdges[0]);
        Graphgen.getNeighboursForGraph(Graphgen.getRandomNumber(nodesAndEdges[0]), nodesAndEdges[0], nodesAndEdges[1]);

        while (true)
        {
            System.out.print(Constants.SELECTION_MENU);
            Scanner sc = new Scanner(System.in);

            String input = sc.next();
            switch (input)
            {
                case "1":
                    sendMessageTo();
                    break;
                case "2":
                    closeNode();
                    break;
                case "3":
                    closeAllNodes();
                    break;
                case "4":

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
        return new int[] {numberOfNodes, numberOfEdges};
    }

    private static String chooseNodeId()
    {
        System.out.println(Constants.REQUEST_ID_INPUT);
        Scanner sc = new Scanner(System.in);
        String input = sc.next();
        return input;
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
        Node.sendMessage(Integer.parseInt(id), Constants.STOP_MESSAGE);
    }

    private static void closeAllNodes()
    {
        int[] allIDs = Node.getAllIds();
        for (int i=0; i< allIDs.length; i++)
        {
            Node.sendMessage(allIDs[i], Constants.STOP_MESSAGE);
        }
    }
}
