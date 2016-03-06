package it.balyfix.rxjava;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

public class SumDouble {

	public static void main(String[] args) {

		Double[] doubleVet = new Double[] { 0d, 5d, 45d };
		Double[] doubleVet1 = new Double[] { 30d, 80d };
		Observable<Double> observable = Observable.from(doubleVet);

		Observable<Double> observable2 = Observable.from(doubleVet1);
		new SumObserver(observable, observable2);

	}

	private static class SumObserver implements Observer<Double> {

		private Double sumResult;

		public SumObserver(Observable<Double> a, Observable<Double> b) {
			sumResult = 0d;

			Observable.combineLatest(a, b, new Func2<Double, Double, Double>() {

				@Override
				public Double call(Double n1, Double n2) {
					return n1 + n2;
				}
			}).subscribe(this);

		}

		@Override
		public void onCompleted() {

			System.out.println("Complete flow result " + getSumResult());

		}

		@Override
		public void onError(Throwable ex) {
			System.err.println("Error with execption " + ex);

		}

		@Override
		public void onNext(Double next) {
			System.out.println("Next value to add " + next);
			sumResult = next;
		}

		public Double getSumResult() {
			return sumResult;
		}
	}

}
