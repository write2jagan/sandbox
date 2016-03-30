package it.balyfix.mahout.raccomander;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

public class MyownRaccomander implements RecommenderBuilder {
	
	private SimilarityMethod userSimilarity;
	private int neighborhoodSize;

	public MyownRaccomander(SimilarityMethod userSimilarity, int neighborhoodSize) {
		this.userSimilarity = userSimilarity;
		this.neighborhoodSize = neighborhoodSize;
	}






	@Override
	public Recommender buildRecommender(DataModel data) throws TasteException {
		
		
		
		
		
		return null;
	}

}
