package kr.ac.kaist.se.artool.search.fitness.value;

public class SmallerBetterFitnessValue extends AtomicFitnessValue {

	public SmallerBetterFitnessValue(float value)
	{
		this.value = value;
	}
	
	@Override
	public int compareTo(FitnessValue f) {
		AtomicFitnessValue o = (AtomicFitnessValue)f;

		if( value < o.value ) return 1;
		else if( value > o.value ) return -1;
		else return 0;
	}

	@Override
	public AtomicFitnessValue getMaxInstance() {
		return new SmallerBetterFitnessValue(Float.NEGATIVE_INFINITY);
	}

	@Override
	public AtomicFitnessValue getMinInstance() {
		return new SmallerBetterFitnessValue(Float.POSITIVE_INFINITY);
	}
}