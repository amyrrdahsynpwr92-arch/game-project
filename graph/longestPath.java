package graph;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class LongestPath {
    private ArrayList<Edge> edges;
    private int maxDistance = 0;
    public LongestPath(ArrayList<Edge> edges){
        this.edges = edges;
    }
    public ArrayList<Edge> setNeighbors(Edge e1){
        ArrayList<Edge> neighbors = new ArrayList<>();
        for(Edge e2: edges){
            if(e1 == e2)continue;
            if((e1.getStart().equals(e2.getStart()) || e1.getEnd().equals(e2.getEnd()) || e1.getStart().equals(e2.getEnd()) || e1.getEnd().equals(e2.getStart())) && e1.getStroke() == e2.getStroke()){
                neighbors.add(e2);
            }
        }
        return neighbors;
    }
    public boolean bfs(Edge edge){
        boolean Increased = false;
        boolean[] visited = new boolean[300];
        Queue<Edge> queue = new LinkedList<>();
        int dis = 1;
        visited[edge.getEdgeNumber()] = true;
        queue.add(edge);
        while(!queue.isEmpty()){
            Edge currentEdge = queue.poll();
            Increased = false;
            for(Edge e: setNeighbors(currentEdge)){
                if(!visited[e.getEdgeNumber()]){
                    visited[e.getEdgeNumber()] = true;
                    queue.add(e);
                    if(!Increased){
                        dis++;
                        Increased = true;
                    }
                }
            }
        }
        if(dis >= 3 && dis > maxDistance){
            maxDistance = dis;
            return true;
        }
        else 
            return false;
    }
    public void setMaxDistance(int maxDistance){
        this.maxDistance = maxDistance;
    }
    public int getMaxDistance(){
        return maxDistance;
    }
}
