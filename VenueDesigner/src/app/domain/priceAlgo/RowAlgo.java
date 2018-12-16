package app.domain.priceAlgo;
import app.domain.seat.Seat;
import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class RowAlgo extends PriceAlgoAbstract{

    @Override
    public void extremeDistribution(List<Section> sections, Point stageCenter, double minPrice, double maxPrice){

        ArrayList<Double> distances = new ArrayList<Double>();
        ArrayList<Seat[]> rows = new ArrayList<Seat[]>();

        double minDist=Double.MAX_VALUE;
        double maxDist=Double.MIN_VALUE;


        for (Section section: sections){
            for (Seat[] row: section.getSeats()){
                double x = row[0].getShape().computeCentroid().x+(row[row.length-1].getShape().computeCentroid().x-
                        row[0].getShape().computeCentroid().x)/2;
                double y = row[0].getShape().computeCentroid().x+(row[row.length-1].getShape().computeCentroid().x-
                        row[0].getShape().computeCentroid().x)/2;
                Point rowCenter = new Point(x,y);
                double distance = Rectangle.distancePoints(rowCenter,stageCenter);
                rows.add(row);
                distances.add(distance);
                minDist = Math.min(minDist, distance);
                maxDist = Math.max(maxDist,distance);
            }
        }

        //répartition des prix
        int k;
        for (int i=0; i<distances.size();i++){
            double rowPrice = computePrice(distances.get(i), minDist, maxDist, minPrice, maxPrice);
            k= (int) computePrice(distances.get(i),minDist,maxDist,0,255);
            for (Seat seat: rows.get(i)){
                seat.setPrice(rowPrice);
                int[] color = {k,0,0,255};
                seat.getShape().setColor(color);
            }
        }

    }
}
