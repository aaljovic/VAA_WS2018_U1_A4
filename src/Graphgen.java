import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Graphgen
{
    private int graphNode;
    private int graphNeighbour;

    public Graphgen(int graphNode, int graphNeighbour)
    {
        this.graphNode = graphNode;
        this.graphNeighbour = graphNeighbour;
    }

    protected static void changeTextFile(int numberOfNodes)
    {
        try
        {
            FileWriter fw = new FileWriter(Constants.TEXT_FILE_NAME);
            BufferedWriter bw = new BufferedWriter(fw);
            int port = 0;
            String zeile = "";
            System.out.println(Constants.TEXT_FILE_MENU_START);
            for (int i=1; i<=numberOfNodes; i++)
            {
                port = 1000 + i;
                zeile = i + " " + "localhost" + ":" + port;
                try
                {
                    System.out.println(zeile);
                    bw.write(zeile);
                    bw.newLine();
                }
                catch (IOException ioe)
                {
                    System.out.println("Datei wird nicht korrekt eingelesen " + ioe);
                }
            }
            bw.close();
            fw.close();
            System.out.println(Constants.FILE_MENU_END);

        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println(Constants.FILE_NOT_FOUND_ERROR + fnfe);
        }
        catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        }
    }

    protected static int getRandomNumber(int limit)
    {
        Random rand = new Random();
        int randomNumber = rand.nextInt(limit) + 1;
        return randomNumber;
    }

    protected static Graphgen getNeighbourForGraph(int id, int numberOfNodes)
    {
        int neighbour = Graphgen.getRandomNumber(numberOfNodes);
        Graphgen neighbours = new Graphgen(id, neighbour);

        return neighbours;
    }


    protected static Graphgen[] getNeighboursForGraph(int randomNumber, int numberOfNodes, int numberOfEdges)
    {
        Graphgen[] neighbours = new Graphgen[numberOfEdges];
        neighbours = Graphgen.initNeighbours(neighbours);
        Graphgen neighbour = new Graphgen(-1, -1);
        int edges = 0;

        while(true)
        {
            for (int i = 1; i <= numberOfNodes; i++)
            {
                int randomNode = getRandomNumber(i);
                Graphgen edge = new Graphgen(i, randomNode);
                if (!checkNeighbourAlreadyExists(neighbours, edge) && (edge.graphNode != edge.graphNeighbour))
                {
                    neighbours[edges] = edge;
                    edges++;
                }
                if (edges == numberOfEdges)
                {
                    return neighbours;
                }
            }
        }
        /*
        for (int i=0; i<numberOfEdges; i++)
        {
            neighbour = Graphgen.getNeighbourForGraph(randomNumber, numberOfNodes);
            if (!checkNeighbourAlreadyExists(neighbours, neighbour))
            {
                neighbours[i] = neighbour;
                System.out.println(neighbours[i].graphNode + " -- " + neighbours[i].graphNeighbour);
            }
            else
            {
                System.out.println("Already exist?");
                //i--;
            }
        }
        */
        //return neighbours;
    }

    protected static void changeGraphFile(int numberOfEdges, Graphgen[] allNeighbours)
    {
        try
        {
            FileWriter fw = new FileWriter(Constants.GRAPH_FILE_NAME);
            BufferedWriter bw = new BufferedWriter(fw);
            int port = 0;
            String zeile = "";
            System.out.println(Constants.GRAPH_FILE_MENU_START);
            System.out.println("graph G {");
            bw.write("graph G {");
            bw.newLine();
            for (int i=0; i<numberOfEdges; i++)
            {
                zeile = allNeighbours[i].graphNode + " -- " + allNeighbours[i].graphNeighbour;
                try
                {
                    System.out.println(zeile);
                    bw.write(zeile);
                    bw.newLine();
                }
                catch (IOException ioe)
                {
                    System.out.println("Datei wird nicht korrekt eingelesen " + ioe);
                }
            }
            System.out.println("}");
            bw.write("}");
            bw.newLine();

            bw.close();
            fw.close();
            System.out.println(Constants.FILE_MENU_END);

        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println(Constants.FILE_NOT_FOUND_ERROR + fnfe);
        }
        catch (IOException ioe)
        {
            System.err.println(Constants.INPUT_OUTPUT_ERROR + ioe);
        }
    }

    protected static Graphgen[] initNeighbours(Graphgen[] neighbours)
    {
        for (int i=0; i<neighbours.length; i++)
        {
            neighbours[i] = new Graphgen(-1, -1);
        }
        return neighbours;
    }

    protected static boolean checkNeighbourAlreadyExists(Graphgen[] neighbours, Graphgen neighbour)
    {
        boolean exist = false;
        for (int i=0; i<neighbours.length; i++)
        {
            if ((neighbours[i].graphNode == neighbour.graphNode) && (neighbours[i].graphNeighbour == neighbour.graphNeighbour))
            {
                exist = true;
            }
            else if ((neighbours[i].graphNode == neighbour.graphNeighbour) && (neighbours[i].graphNeighbour == neighbour.graphNode))
            {
                exist = true;
            }
        }
        return exist;
    }

    protected static boolean checkValueInArray(int[] array, int value)
    {
        boolean exist = false;
        for (int i=0; i<array.length; i++)
        {
            if (array[i] == value)
            {
                exist = true;
            }
        }
        return exist;
    }
}
