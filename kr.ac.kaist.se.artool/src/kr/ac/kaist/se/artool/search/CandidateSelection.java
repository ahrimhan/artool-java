package kr.ac.kaist.se.artool.search;

import java.util.Set;
import java.util.Vector;

import kr.ac.kaist.se.artool.engine.refactoring.MoveMethodCommand;

public interface CandidateSelection {
	Set<MoveMethodCommand> getCandidates();
}
