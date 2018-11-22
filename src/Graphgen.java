import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Graphgen
{
    private int knoten;
    private int nachbar;

    public Graphgen(int knoten, int nachbar)
    {
        this.knoten = knoten;
        this.nachbar = nachbar;
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
            System.out.println(Constants.TEXT_FILE_MENU_END);

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
        Graphgen[] neighbours = new Graphgen[10];
        Graphgen neighbour;
        for (int i=0; i<numberOfEdges; i++)
        {
            neighbour = Graphgen.getNeighbourForGraph(randomNumber, numberOfNodes);
            if (!checkNeighbourAlreadyExists(neighbours, neighbour))
            {
                neighbours[i] = neighbour;
                System.out.println(neighbours[i].knoten + " -- " + neighbours[i].nachbar);
            }
            else
            {
                i--;
            }
        }
        return neighbours;
    }

    protected static boolean checkNeighbourAlreadyExists(Graphgen[] neighbours, Graphgen neighbour)
    {
        System.out.println(neighbours.length);
        boolean exist = false;
        for (int i=0; i<neighbours.length; i++)
        {
            System.out.println("neighbours[i].knoten " + neighbours[i].knoten);
            System.out.println("neighbour.knoten " + neighbour.knoten);
            if ((neighbours[i].knoten == neighbour.knoten) && (neighbours[i].nachbar == neighbour.nachbar))
            {
                exist = true;
            }
            else if ((neighbours[i].knoten == neighbour.nachbar) && (neighbours[i].nachbar == neighbour.knoten))
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
