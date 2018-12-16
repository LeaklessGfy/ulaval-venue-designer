package app.domain.priceAlgo;

public class PriceAlgoFactory {
    private static PriceAlgoFactory factory;
    private PriceAlgoFactory(){
        factory = this;
    }
    public static PriceAlgoFactory get(){
        if (factory == null){return new PriceAlgoFactory();}
        return factory;
    }

    public PriceAlgo create(AlgoType type){
        if (type==AlgoType.Seat){return new SeatAlgo();}
        else if (type==AlgoType.Row){return new RowAlgo();}
        return new SectionAlgo();
    }
}
