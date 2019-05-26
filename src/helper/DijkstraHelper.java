package helper;

import model.Jadwal;
import model.Jarak;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DijkstraHelper {
    private int[] distances;
    private Set<Integer> settled;
    private Set<Integer> unsettled;
    private int nodes;
    private int[][] arrayMatrix;
    private static List<Jarak> output;
    private static Jarak ruangan;

    private DijkstraHelper(int nodes) {
        this.nodes = nodes;
        distances = new int[nodes + 1];
        settled = new HashSet<>();
        unsettled = new HashSet<>();
        arrayMatrix = new int[nodes + 1][nodes + 1];
        output = new ArrayList<>();
        ruangan = new Jarak(0, 0, 0);
    }

    private void generateDijkstra(int[][] matrix, int source) {
        int node;
        for (int i = 1; i <= nodes; i++)
            if (nodes >= 0) System.arraycopy(matrix[i], 1, arrayMatrix[i], 1, nodes);

        for (int i = 1; i <= nodes; i++)
        {
            distances[i] = Integer.MAX_VALUE;
        }

        unsettled.add(source);
        distances[source] = 0;
        while (!unsettled.isEmpty())
        {
            node = getMinDisFromUnsettled();
            unsettled.remove(node);
            settled.add(node);
            getMinNode(node);
        }
    }

    private int getMinDisFromUnsettled() {
        int min ;
        int node;

        Iterator<Integer> iterator = unsettled.iterator();
        node = iterator.next();
        min = distances[node];
        for (int i = 1; i <= distances.length; i++)
        {
            if (unsettled.contains(i))
            {
                if (distances[i] <= min)
                {
                    min = distances[i];
                    node = i;
                }
            }
        }
        return node;
    }

    private void getMinNode(int node) {
        int edge;
        int newDistance;

        for (int destinationNode = 1; destinationNode <= nodes; destinationNode++) {
            if (!settled.contains(destinationNode)) {
                if (arrayMatrix[node][destinationNode] != Integer.MAX_VALUE) {
                    edge = arrayMatrix[node][destinationNode];
                    newDistance = distances[node] + edge;
                    if (newDistance < distances[destinationNode])
                    {
                        distances[destinationNode] = newDistance;
                    }
                    unsettled.add(destinationNode);
                }
            }
        }
    }

    public static void main(int kelas, int hari) throws SQLException {
        checkJadwal(kelas, hari);
    }

    private static void fillList(int source, int destination, int weight){
        Jarak jarak = new Jarak(source, destination, weight);
        output.add(jarak);
    }

    private static void compare(int node) throws SQLException {
        boolean checked = false;
        for(int i=0; i<node; i++){
            for (int j=i+1; j<node; j++){
                if (output.get(i).getDestination()!=1){
                    if (!checked){
                        if (output.get(i).getWeight() < output.get(j).getWeight()) {
                            ruangan.setSource(output.get(i).getSource());
                            ruangan.setDestination(output.get(i).getDestination());
                            ruangan.setWeight(output.get(i).getWeight());
                            checked = true;
                        }
                    } else {
                        if (ruangan.getWeight() > output.get(i).getWeight()) {
                            ruangan.setSource(output.get(i).getSource());
                            ruangan.setDestination(output.get(i).getDestination());
                            ruangan.setWeight(output.get(i).getWeight());
                        }
                    }
                }
            }
        }

        print();
    }

    private static void checkJadwal(int kelas, int hari) throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM jadwal WHERE no_kelas='" + kelas + "' AND no_hari='" + hari + "' ORDER BY no_hari DESC, no_sesi DESC");

        List<Jadwal> listJadwal = new ArrayList<>();
        int no_matkul;

        while (rs.next()){
            no_matkul = rs.getInt("no_matkul");

            System.out.println("matkul: " + no_matkul);
            if (no_matkul == 28 || no_matkul == 29 || no_matkul == 30 || no_matkul == 31 ||
                    no_matkul == 32 || no_matkul == 33 || no_matkul ==34){
                System.out.println("Preference Matkul - " + no_matkul);
            } else {
                Jadwal jadwal = new Jadwal();
                jadwal.setId(rs.getString("id_jadwal"));
                jadwal.setRuangan(rs.getString("no_ruangan"));
                jadwal.setHari(rs.getString("no_hari"));
                jadwal.setKelas(rs.getString("no_kelas"));

                listJadwal.add(jadwal);
            }
        }

        System.out.println(listJadwal.toString());

        for (int i = 0; i< listJadwal.size(); i++){
            if (i!= listJadwal.size()-1){
                processDijkstra(Integer.parseInt(listJadwal.get(i).getRuangan()));
                listJadwal.get(i+1).setRuangan(String.valueOf(ruangan.getDestination()));
                updateJadwal(Integer.parseInt(listJadwal.get(i+1).getId()), ruangan.getDestination());
            }
        }
    }

    private static void updateJadwal(int id, int ruangan){
        SQLHelper.insertDB("UPDATE ruangan SET status='2' WHERE no='" + ruangan + "'");
        SQLHelper.insertDB("UPDATE jadwal SET no_ruangan='" + ruangan + "' WHERE id_jadwal='" + id + "'");
    }

    private static void processDijkstra(int source) throws SQLException {
        int[][] adjacency_matrix;
        int number_of_vertices = 0;

        try
        {
            ResultSet r = SQLHelper.getResultSet("SELECT * FROM ruangan");
            ResultSet rss = SQLHelper.getResultSet("SELECT * FROM jarak_ruangan");

            while (r.next()){
                number_of_vertices++;
            }

            adjacency_matrix = new int[number_of_vertices + 1][number_of_vertices + 1];

            while (rss.next()){
                int nodeA = rss.getInt("source");
                int nodeB = rss.getInt("destination");
                int weight = rss.getInt("weight");

                adjacency_matrix[nodeA][nodeB] = weight;
                if (nodeA == nodeB) {
                    adjacency_matrix[nodeA][nodeB] = 0;
                    continue;
                }
                if (adjacency_matrix[nodeA][nodeB] == 0){
                    adjacency_matrix[nodeA][nodeB] =  Integer.MAX_VALUE;
                }
            }

            DijkstraHelper dijkstrasAlgorithm = new DijkstraHelper(number_of_vertices);
            dijkstrasAlgorithm.generateDijkstra(adjacency_matrix, source);

            for (int i = 2; i <= dijkstrasAlgorithm.distances.length - 1; i++){
                ResultSet resultSet = SQLHelper.getResultSet("SELECT * FROM ruangan WHERE no='" + i + "'");
                int status=0;

                while (resultSet.next()) status = resultSet.getInt("status");

                if (status == 1) fillList(source, i, dijkstrasAlgorithm.distances[i]);
            }

            compare(output.size());
        } catch (InputMismatchException inputMismatch) {
            System.out.println("Wrong Input Format");
        }
    }

    private static void print() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM ruangan WHERE no='" + ruangan.getDestination() + "'");
        String source = "", destination = "";
        while (rs.next()){
            destination = rs.getString("nama");
        }

        rs = SQLHelper.getResultSet("SELECT * FROM ruangan WHERE no='" + ruangan.getSource() + "'");
        while (rs.next()){
            source = rs.getString("nama");
        }

        System.out.println(ruangan.getSource() + "(" + source + ") -> " + ruangan.getDestination() + "(" + destination + ")");
    }
}
