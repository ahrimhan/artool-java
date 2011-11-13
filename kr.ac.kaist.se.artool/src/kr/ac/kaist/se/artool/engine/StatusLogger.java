package kr.ac.kaist.se.artool.engine;

import java.text.NumberFormat;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Vector;

import kr.ac.kaist.se.artool.engine.rules.AbstractRule;

import org.eclipse.emf.common.util.BasicEMap;

public class StatusLogger {
	private static StatusLogger instance = null;
	private Stack<BasicEMap<String, float[]>> log;
	private BasicEMap<AbstractRule, BasicEMap<String, float[]>> trials;
	
	private static Vector<BasicEMap<String, float[]>> emapPool = new Vector<BasicEMap<String, float[]>>();
	private static Vector<float[]> floatPool = new Vector<float[]>();
	
	static
	{
		for( int i = 0; i < 1000; i++ )
		{
			emapPool.add(new BasicEMap<String, float[]>());
			floatPool.add(new float[1]);
		}
	}
	
	static float[] getFloat()
	{
		if( floatPool.isEmpty() )
		{
			for( int i = 0; i < 1000; i++ )
			{
				floatPool.add(new float[1]);
			}
		}
		
		return floatPool.remove(0);
	}
	
	static void returnFloat(float[] f)
	{
		floatPool.add(f);
	}
	
	static BasicEMap<String, float[]> getEmap()
	{
		if( emapPool.isEmpty() )
		{
			for( int i = 0; i < 1000; i++ )
			{
				emapPool.add(new BasicEMap<String, float[]>());
			}
		}
		
		return emapPool.remove(0);
	}
	
	static void returnEmap(BasicEMap<String, float[]> emap)
	{
		emap.clear();
		emapPool.add(emap);
	}
		
	public void clear()
	{
		log.clear();
		trials.clear();
	}
	
	public static StatusLogger getInstance()
	{
		if( instance == null )
		{
			instance = new StatusLogger();
		}
		return instance;
	}
	
	private StatusLogger()
	{
		log = new Stack<BasicEMap<String, float[]>>();
		trials = new BasicEMap<AbstractRule, BasicEMap<String, float[]>>();
	}
	
	
	public void openTrialPhase()
	{
		for( BasicEMap<String, float[]> vv : trials.values() )
		{	
			for( float[] vvv : vv.values() )
			{
				returnFloat(vvv);
			}
			returnEmap(vv);
		}
		trials.clear();
	}
	
	public void openTrialSuite(AbstractRule rule)
	{
		trials.put(rule, getEmap());
	}
	
	
	public void selectSuite(AbstractRule rule)
	{
		if( log.size() > 1 )
		{
			log.pop();
		}
		log.push(trials.get(rule));
	}
	
	public void openOriginalPhase()
	{
		log.push(new BasicEMap<String, float[]>());
	}
	
	public void putVar(String var, float value)
	{
		if( getCurrentSuite().containsKey(var) )
		{
			float[] f = getCurrentSuite().get(var);
			f[0] = value;
		}
		else
		{
			float[] f = getFloat();
			f[0] = value;
			getCurrentSuite().put(var, f);
		}
	}
	
	public void printCurrentSuite()
	{
		ListIterator<Entry<String, float[]>> iterator = getCurrentSuite().listIterator();
		float value = 0;
	
		while( iterator.hasNext())
		{
			value = iterator.next().getValue()[0];
			System.err.print(value + "\t");
			//System.err.print(Float.toString(value) + "\t");
			NumberFormat nf= NumberFormat.getInstance();
			
			nf.setMaximumFractionDigits(8);
			nf.setMinimumFractionDigits(8);
			ARToolMain.getInstance().getPrintStream().print(nf.format(value) + "\t");
		}
		System.err.println();
		ARToolMain.getInstance().getPrintStream().println();
	}
	
	public BasicEMap<String, float[]> getPreviousPhase()
	{
		return log.peek();
	}
	
	public BasicEMap<String, float[]> getTrialPhase(AbstractRule rule)
	{
		return trials.get(rule);
	}
	
	public BasicEMap<String, float[]> getOriginalPhase()
	{
		return log.firstElement();
	}
	
	public BasicEMap<String, float[]> getCurrentSuite()
	{
		if( trials.size() == 0 )
		{
//			System.err.println("initialize!!!");
			return log.peek();
		}
		
		BasicEMap<String, float[]> ret = trials.get(trials.size()-1).getValue();
		
		return ret;
	}
	
	private void _printDelta(BasicEMap<String, float[]> previous)
	{
		ListIterator<Entry<String, float[]>> iterator = getCurrentSuite().listIterator();
		
		while( iterator.hasNext())
		{
			Entry<String, float[]> curEntry = iterator.next();
			float prev_value = 0;
			if( previous.get(curEntry.getKey()) != null )
			{
				prev_value = previous.get(curEntry.getKey())[0];
			}
			float diff = curEntry.getValue()[0] - prev_value;
			NumberFormat nf= NumberFormat.getInstance();
			
			nf.setMaximumFractionDigits(8);
			nf.setMinimumFractionDigits(8);
			
			System.err.print(nf.format(diff) + "\t");
		}
		System.err.println();
	}
	
	public void printDeltaWithPrevious()
	{
		BasicEMap<String, float[]> previous = getPreviousPhase();
		_printDelta(previous);
		
	}
	
	public void printDeltaWithOriginal()
	{
		BasicEMap<String, float[]> original = log.firstElement();
		_printDelta(original);
	}
}
