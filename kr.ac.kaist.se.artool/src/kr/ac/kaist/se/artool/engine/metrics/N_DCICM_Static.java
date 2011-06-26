package kr.ac.kaist.se.artool.engine.metrics;

import java.awt.List;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import kr.ac.kaist.se.aom.AbstractObjectModel;
import kr.ac.kaist.se.aom.dynamicmodel.DynamicMethodCall;
import kr.ac.kaist.se.aom.staticmodel.StaticMethodCall;
import kr.ac.kaist.se.aom.structure.AOMClass;
import kr.ac.kaist.se.aom.structure.AOMMethod;

public class N_DCICM_Static extends N_DCICM {
	public N_DCICM_Static(){
		super();
	}
	
	public static final String N_DCICM_Static = "N_DCICM_Static";
	
	
	public String getName()
	{
		return N_DCICM_Static;
	}

	protected int helpMeasure(int ndcicm, AOMMethod method,
			HashSet<AOMClass> visitedClass, HashSet<AOMMethod> visitedMethod) {
		for( StaticMethodCall dmc : method.getOwnedScope().getStaticMethodCalls() )
		{
			if( !visitedClass.contains(dmc.getCallee().getOwner()) )
			{
				ndcicm++;
				visitedClass.add(dmc.getCallee().getOwner());
				visitedMethod.add(dmc.getCallee());
			
			}
		}
		return ndcicm;
	}
}
