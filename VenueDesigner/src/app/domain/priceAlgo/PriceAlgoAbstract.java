package app.domain.priceAlgo;

abstract class PriceAlgoAbstract implements PriceAlgo{
    protected static double computePrice( double dist, double minDist, double maxDist,double minPrice, double maxPrice){
        double dx = maxDist-minDist;
        double dy = maxPrice-minPrice;

        double price = minPrice + dy * (maxDist-dist)/dx;
        return price;
    }
}
