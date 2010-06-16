// This file is part of AceWiki.
// Copyright 2008-2010, Tobias Kuhn.
// 
// AceWiki is free software: you can redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
// 
// AceWiki is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
// even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with AceWiki. If
// not, see http://www.gnu.org/licenses/.

package ch.uzh.ifi.attempto.chartparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a chart parser (concretely an Earley parser) fully implemented in Java.
 * 
 * @see Grammar
 * @author Tobias Kuhn
 */
public class ChartParser {
	
	private final Grammar grammar;
	private final String startCategoryName;
	private final Chart chart;
	private final List<Terminal> tokens = new ArrayList<Terminal>();
	private final List<NextTokenOptions> options = new ArrayList<NextTokenOptions>();
	private final List<List<FeatureMap>> backwardReferences = new ArrayList<List<FeatureMap>>();
	private boolean debug;
	
	/**
	 * Creates a new chart parser for the given grammar. The grammar must not be changed afterwards.
	 * 
	 * @param grammar The grammar to be used by the chart parser.
	 * @param startCategoryName The name of the start category.
	 */
	public ChartParser(Grammar grammar, String startCategoryName) {
		this.grammar = grammar;
		this.startCategoryName = startCategoryName;
		this.chart = new Chart(grammar);
		options.add(null);
		init();
		runParsingSteps();
	}
	
	/**
	 * This method can be used to switch on/off debug mode (default is off). In debug mode, messages
	 * about the actions of the chart parser are printed onto the standard error device.
	 * 
	 * @param debug true to switch debug mode on, or false to switch it off.
	 */
	public void debug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Adds the token to the end of the token sequence and runs the parsing algorithm on it.
	 * 
	 * @param token The new token to be added.
	 */
	public void addToken(Terminal token) {
		addToken(token, null);
	}
	
	/**
	 * Adds the token to the end of the token sequence together with its preterminal categories,
	 * and runs the parsing algorithm on them. If several pre-terminal categories are given then
	 * these categories are treated in a parallel way, i.e. only one of the pre-terminal categories
	 * must match.
	 * 
	 * @param token The token to be added to the token sequence.
	 * @param categories The pre-terminal categories.
	 */
	public void addToken(Terminal token, Collection<Preterminal> categories) {
		// add the token to the list of tokens:
		tokens.add(token);
		if (debug) {
			log("ADD TOKEN: " + token + "\nTOKEN LIST:");
			for (Terminal t : tokens) log(" " + t);
			log("\n");
		}
		options.add(null);
		backwardReferences.add(new ArrayList<FeatureMap>());
		
		// add a new edge to the chart for the new token:
		if (categories == null) {
			Edge edge = new Edge(tokens.size()-1, token.deepCopy());
			chart.addEdge(edge);
			if (debug) log("SCANNER: " + edge + "\n");
		} else {
			for (Preterminal p : categories) {
				Edge edge = new Edge(tokens.size()-1, p.deepCopy());
				chart.addEdge(edge);
				if (debug) log("SCANNER: " + edge + "\n");
			}
		}
		
		// add edges for applicable lexical rules:
		for (LexicalRule lexRule : grammar.getLexRulesByWord(token.getName())) {
			Edge edge = new Edge(tokens.size()-1, lexRule.deepCopy());
			chart.addEdge(edge);
			if (debug) log("SCANNER: " + edge + "\n");
		}
		
		runParsingSteps();
		//if (debug) log("CHART:");
		//if (debug) log(chart);
	}
	
	/**
	 * Removes the last token and reverts the last parsing step.
	 */
	public void removeToken() {
		chart.removeEdgesWithEndPos(tokens.size());
		backwardReferences.remove(tokens.size()-1);
		options.remove(tokens.size());
		tokens.remove(tokens.size()-1);
		if (debug) {
			log("REMOVE LAST TOKEN.\nTOKEN LIST:");
			for (Terminal t : tokens) log(" " + t);
			log("\n");
		}
	}
	
	/**
	 * Removes all tokens in the current token sequence and resets the chart.
	 */
	public void removeAllTokens() {
		if (debug) log("REMOVE ALL TOKENS.\n");
		tokens.clear();
		options.clear();
		options.add(null);
		backwardReferences.clear();
		chart.clear();
		init();
		runParsingSteps();
	}
	
	/**
	 * Returns the current token sequence.
	 * 
	 * @return The current token sequence.
	 */
	public List<Terminal> getTokens() {
		return new ArrayList<Terminal>(tokens);
	}
	
	/**
	 * Returns the number of tokens of the current (partial) text.
	 * 
	 * @return The number of tokens.
	 */
	public int getTokenCount() {
		return tokens.size();
	}
	
	/**
	 * Return a list of feature maps that show how the backward references at the given position
	 * in the text can be resolved. These feature maps contain special features of the form "*pos"
	 * that denote the textual position of the respective forward references.
	 * 
	 * @param pos The position of the backward reference.
	 * @return The list of feature maps.
	 */
	public List<FeatureMap> getBackwardReferences(int pos) {
		if (pos == -1 || pos >= tokens.size()) {
			return new ArrayList<FeatureMap>();
		}
		return backwardReferences.get(pos);
	}
	
	/**
	 * Returns a list of feature maps that show how the backward references at the end of the
	 * token sequence can be resolved.
	 * 
	 * @return The list of feature maps.
	 */
	public List<FeatureMap> getBackwardReferences() {
		return getBackwardReferences(tokens.size()-1);
	}
	
	/**
	 * Returns true if the current token sequence is a complete statement according to the
	 * grammar.
	 * 
	 * @return true if the current token sequence is a complete statement.
	 */
	public boolean isComplete() {
		for (Edge e : chart.getEdgesByEndPos(tokens.size())) {
			if (!e.isActive() && e.getHead().getName().equals(startCategoryName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This methods shows the possible tokens that could be used to continue the text at the given
	 * position.
	 * 
	 * @param position The position at which the possible next tokens should be found.
	 * @return The options describing the possible next tokens.
	 */
	public NextTokenOptions getNextTokenOptions(int position) {
		createOptions(position);
		return options.get(position);
	}
	
	/**
	 * This method returns the possible next tokens that could be used to continue the text at the
	 * end position.
	 * 
	 * @return The options describing the possible next tokens.
	 */
	public NextTokenOptions getNextTokenOptions() {
		return getNextTokenOptions(tokens.size());
	}
	
	/**
	 * This method returns a set of abstract options describing the possible next tokens at the
	 * given position in an abstract way.
	 * 
	 * @param position The position at which the possible next tokens should be found.
	 * @return The set of abstract options describing the possible next tokens.
	 */
	public Set<AbstractOption> getAbstractOptions(int position) {
		createOptions(position);
		return options.get(position).getAbstractOptions();
	}
	
	/**
	 * This method returns a set of abstract options describing the possible next tokens at the end
	 * position in an abstract way.
	 * 
	 * @return The set of abstract options describing the possible next tokens.
	 */
	public Set<AbstractOption> getAbstractOptions() {
		return getAbstractOptions(tokens.size());
	}

	/**
	 * This method returns a set of concrete options describing the possible next tokens at the
	 * given position in a concrete way.
	 * 
	 * @param position The position at which the possible next tokens should be found.
	 * @return The set of concrete options describing the possible next tokens.
	 */
	public Set<ConcreteOption> getConcreteOptions(int position) {
		createOptions(position);
		return options.get(position).getConcreteOptions();
	}

	/**
	 * This method returns a set of concrete options describing the possible next tokens at the end
	 * position in a concrete way.
	 * 
	 * @return The set of concrete options describing the possible next tokens.
	 */
	public Set<ConcreteOption> getConcreteOptions() {
		return getConcreteOptions(tokens.size());
	}
	
	/**
	 * Creates the abstract and concrete options at the given position. The options are cached.
	 * 
	 * @param position The position for which the options should be calculated.
	 */
	private void createOptions(int position) {
		if (options.get(position) == null) {
			Set<AbstractOption> aOptions = createAbstractOptions(position);
			Set<ConcreteOption> cOptions = createConcreteOptions(position, aOptions);
			options.set(position, new NextTokenOptions(aOptions, cOptions));
		}
	}
	
	/**
	 * Calculates the set of abstract options for the given position.
	 * 
	 * @param position The position for which the abstract options should be calculated.
	 * @return The set of abstract options.
	 */
	private Set<AbstractOption> createAbstractOptions(int position) {
		Set<AbstractOption> aOptions = new HashSet<AbstractOption>();
		for (Edge e : chart.getEdgesByEndPos(position)) {
			if (!e.isActive()) continue;
			if (e.getNextActive() instanceof Nonterminal) continue;
			
			BackrefCategory backref = null;
			Nonterminal negbackref = null;
			int refpos = 0;
			Category[] body = e.getBody();
			int p = e.getProgress();
			for (int i = p + 1 ; i < body.length ; i++) {
				Category c = body[i];
				if (!(c instanceof Nonterminal)) continue;
				if (c instanceof BackrefCategory) {
					backref = (BackrefCategory) c;
					refpos = i;
				} else if (i == (p+1) && c.getName().equals("/<")) {
					negbackref = (Nonterminal) c;
					refpos = i;
				}
				break;
			}
			
			if (backref != null) {
				// For edges with backwards references, the possible bindings have to be performed:
				for (int i = e.getCombinedAnteList().length - 1 ; i >= 0 ; i--) {
					if (e.getCombinedAnteList()[i].getName().equals("//")) continue;
					
					int posrefsCount = backref.getPosFeatureMaps().size();
					int negrefsCount = backref.getNegFeatureMaps().size();
					List<Category> exceptions = null;
					boolean makeRestriction = true;
					
					if (refpos == (p+1)) {
						exceptions = new ArrayList<Category>();
						for (int j = 0 ; j < negrefsCount ; j++) {
							Edge eC = e.deepCopy();
							try {
								FeatureMap backrefFm =
									((BackrefCategory) eC.getBody()[refpos]).getNegFeatureMaps().get(j);
								eC.getCombinedAnteList()[i].getFeatureMap().unify(backrefFm);
								if (eC.getNextActive() instanceof Terminal) {
									makeRestriction = false;
									break;
								} else {
									exceptions.add(eC.getNextActive());
								}
							} catch (UnificationFailedException ex) {}
						}
					}
					if (!makeRestriction) break;
					
					for (int j = 0 ; j < posrefsCount ; j++) {
						Edge eC = e.deepCopy();
						try {
							FeatureMap backrefFm =
								((BackrefCategory) eC.getBody()[refpos]).getPosFeatureMaps().get(j);
							eC.getCombinedAnteList()[i].getFeatureMap().unify(backrefFm);
							if (exceptions != null) {
								aOptions.add(new AbstractOption(
										grammar,
										eC.getNextActive(),
										copyExceptionsList(exceptions)
									));
							} else {
								aOptions.add(new AbstractOption(grammar, eC.getNextActive()));
							}
						} catch (UnificationFailedException ex) {}
					}
				}
			} else if (negbackref != null) {
				List<Category> exceptions = new ArrayList<Category>();
				// Edges with negative backwards references lead to exceptions:
				boolean makeRestriction = true;
				for (int i = 0 ; i < e.getCombinedAnteList().length ; i++) {
					if (e.getCombinedAnteList()[i].getName().equals("//")) continue;
					Edge eC = e.deepCopy();
					try {
						eC.getCombinedAnteList()[i].getFeatureMap().unify(eC.getBody()[refpos].getFeatureMap());
						if (eC.getNextActive() instanceof Terminal) {
							makeRestriction = false;
							break;
						} else {
							exceptions.add(eC.getNextActive());
						}
					} catch (UnificationFailedException ex) {}
				}
				if (makeRestriction) {
					aOptions.add(new AbstractOption(grammar, e.getNextActive().deepCopy(), exceptions));
				}
			} else {
				aOptions.add(new AbstractOption(grammar, e.getNextActive().deepCopy()));
			}
		}
		if (debug) {
			for (AbstractOption o : aOptions) {
				log("LOOKING FORWARD: " + o + "\n");
			}
		}
		
		return aOptions;
	}

	/**
	 * Calculates the set of concrete options for the given position on the basis of a set of
	 * abstract options.
	 * 
	 * @param position The position for which the concrete options should be calculated.
	 * @param aOptions The set of abstract options.
	 * @return The set of concrete options.
	 */
	private Set<ConcreteOption> createConcreteOptions(int position, Set<AbstractOption> aOptions) {
		Set<ConcreteOption> cOptions = new HashSet<ConcreteOption>();
		
		for (AbstractOption ao : aOptions) {
			if (ao.getCategory() instanceof Preterminal) {
				for (LexicalRule lexRule : grammar.getLexRulesByCatName(ao.getCategory().getName())) {
					if (ao.isFulfilledBy(lexRule.getCategory())) {
						cOptions.add(new ConcreteOption(grammar, lexRule.deepCopy()));
					}
				}
			} else if (ao.getCategory() instanceof Terminal) {
				cOptions.add(new ConcreteOption(grammar, (Terminal) ao.getCategory(), null));
			}
		}
		
		return cOptions;
	}
	
	/**
	 * Runs the initialization step of the Earley parsing algorithm.
	 */
	private void init() {
		for (GrammarRule rule : grammar.getRulesByHeadName(startCategoryName)) {
			Edge edge = new Edge(0, rule.deepCopy());
			chart.addEdge(edge);
			if (debug) log("INIT: " + rule + "  --->  " + edge + "\n");
		}
	}
	
	/**
	 * Runs the main parsing steps of the Earley algorithm. These parsing steps consists of the
	 * completion/prediction/resolution loop.
	 */
	private void runParsingSteps() {
		// Run completion/predition/resolution until neither of them generates a new edge:
		int chartSize = 0;
		int step = 0;
		int idleSteps = 0;
		Map<String, Integer> progressTable = new HashMap<String, Integer>();
		progressTable.put("prediction", 0);
		progressTable.put("completion", 0);
		progressTable.put("resolution", 0);
		while (true) {
			step++;
			chartSize = chart.getSize();
			if (step == 1) {
				predict(progressTable);
			} else if (step == 2) {
				resolve(progressTable);
			} else {
				complete(progressTable);
				step = 0;
			}
			if (chartSize == chart.getSize()) {
				idleSteps++;
			} else {
				idleSteps = 0;
			}
			if (idleSteps > 2) {
				break;
			}
		}
	}
	
	/**
	 * Runs the prediction step of the Earley parsing algorithm.
	 * 
	 * @param progressTable This table captures the progress state in order to prevent from
	 *     checking the same edges more than once.
	 */
	private void predict(Map<String, Integer> progressTable) {
		List<Edge> l = chart.getEdgesByEndPos(tokens.size());
		for (int i = new Integer(progressTable.get("prediction")) ; i < l.size() ; i++) {
			// During this loop, elements might be added to the end of the list l.
			
			Edge existingEdge = l.get(i);
			Category category = existingEdge.getNextActive();
			if (category == null) continue;
			if (category instanceof Terminal) continue;
			if (category.isSpecialCategory()) continue;
			if (debug) log("PREDICTION FOR CATEGORY: " + category + "\n");
			
			for (GrammarRule rule : grammar.getRulesByHeadName(category.getName())) {
				try {
					if (!category.isSimilar(rule.getHead())) continue;
					Edge edgeC = existingEdge.deepCopy();
					GrammarRule ruleC = rule.deepCopy();
					edgeC.getNextActive().unify(ruleC.getHead());
					Edge edge = new Edge(tokens.size(), ruleC, edgeC.getCombinedAnteList());
					boolean isNewEdge = chart.addEdge(edge);
					if (debug) log("PREDICT (" + (isNewEdge ? "NEW" : "KNOWN") + "): " + rule + "  --->  " + edge + "\n");
				} catch (UnificationFailedException ex) {
					continue;
				}
			}
		}
		progressTable.put("prediction", l.size());
	}
	
	/**
	 * Runs the completion step of the Earley parsing algorithm.
	 * 
	 * @param progressTable This table captures the progress state in order to prevent from
	 *     checking the same edges more than once.
	 */
	private void complete(Map<String, Integer> progressTable) {
		List<Edge> l1 = chart.getEdgesByEndPos(tokens.size());
		for (int i1 = 0 ; i1 < l1.size() ; i1++) {
			// During this loop, elements might be added to the end of the list l1.
			
			Edge passiveEdge = l1.get(i1);
			if (passiveEdge.isActive()) continue;
			if (debug) log("COMPLETION FOR EDGE: " + passiveEdge + "\n");

			List<Edge> l2 = chart.getEdgesByEndPos(passiveEdge.getStartPos());
			int start;
			if (i1 < progressTable.get("completion")) {
				Integer progress = progressTable.get("completion " + i1);
				if (progress == null) {
					start = 0;
				} else {
					start = progress;
				}
			} else {
				start = 0;
			}
			
			for (int i2 = start ; i2 < l2.size() ; i2++) {
				// During this loop, elements might be added to the end of the list l2.
				
				Edge edge = l2.get(i2);
				if (!edge.isActive()) continue;
				if (!edge.getNextActive().getName().equals(passiveEdge.getHead().getName())) continue;
				
				try {
					if (!passiveEdge.getHead().isSimilar(edge.getNextActive())) continue;
					Edge passiveEdgeC = passiveEdge.deepCopy();
					Edge edgeC = edge.deepCopy();
					passiveEdgeC.getHead().unify(edgeC.getNextActive());
					if (!passiveEdge.carriesAntecedentInformation()) {
						// Antecedent lists have to match:
						Category[] al1 = edgeC.getCombinedAnteList();
						Category[] al2 = passiveEdgeC.getExternalAnteList();
						if (al1.length != al2.length) throw new UnificationFailedException();
						for (int i = 0 ; i < al1.length ; i++) {
							al1[i].unify(al2[i]);
						}
					}
					edgeC.step(tokens.size(), passiveEdgeC);
					boolean isNewEdge = chart.addEdge(edgeC);
					if (debug) log("COMPLETE (" + (isNewEdge ? "NEW" : "KNOWN") + "): " + edge + "  --->  " + edgeC + "\n");
				} catch (UnificationFailedException ex) {
					continue;
				}
			}
			progressTable.put("completion " + i1, l2.size());
		}
		progressTable.put("completion", l1.size());
	}

	/**
	 * Runs the resolution step, which is an extension of the standard Earley algorithm.
	 * 
	 * @param progressTable This table captures the progress state in order to prevent from
	 *     checking the same edges more than once.
	 */
	private void resolve(Map<String, Integer> progressTable) {
		List<Edge> l1 = chart.getEdgesByEndPos(tokens.size());
		for (int i1 = progressTable.get("resolution") ; i1 < l1.size() ; i1++) {
			// During this loop, elements might be added to the end of the list l1.
			
			Edge edge = l1.get(i1);
			if (!edge.isActive()) continue;
			
			String n = edge.getNextActive().getName();
			List<Edge> newEdges = new ArrayList<Edge>();
			if (n.equals("#")) {
				Edge edgeC = edge.deepCopy();
				try {
					edgeC.getNextActive().getFeature("pos").unify(new StringRef("#" + tokens.size()));
					newEdges.add(edgeC);
				} catch (UnificationFailedException ex) {}
			} else if (n.equals(">") || n.equals(">>") || n.equals("//")) {
				Edge edgeC = edge.deepCopy();
				edgeC.getNextActive().setFeature("*pos", tokens.size() + "");
				edgeC.addAntecedents(edgeC.getNextActive());
				newEdges.add(edgeC);
			} else if (n.equals("<")) {
				BackrefCategory bwrefCat = (BackrefCategory) edge.getNextActive();
				Category[] ante = edge.getCombinedAnteList();
				for (int i = ante.length-1 ; i >= 0 ; i--) {
					if (ante[i].getName().equals("//")) continue;
					
					boolean negMatch = false;
					for (FeatureMap negfm : bwrefCat.getNegFeatureMaps()) {
						if (ante[i].getFeatureMap().canUnify(negfm)) {
							negMatch = true;
							break;
						}
					}
					if (negMatch) continue;
					
					boolean posMatch = false;
					List<FeatureMap> posfms = bwrefCat.getPosFeatureMaps();
					for (int j = 0 ; j < posfms.size() ; j++) {
						if (ante[i].getFeatureMap().canUnify(posfms.get(j))) {
							try {
								Edge edgeC = edge.deepCopy();
								edgeC.getExternalAnteList()[i].getFeatureMap().unify(
										((BackrefCategory) edgeC.getNextActive()).getPosFeatureMaps().get(j)
									);
								backwardReferences.get(tokens.size()-1).add(
										edgeC.getExternalAnteList()[i].getFeatureMap().deepCopy()
									);
								newEdges.add(edgeC);
								posMatch = true;
							} catch (UnificationFailedException ex) {}
						}
					}
					if (posMatch) break;
				}
			} else if (n.equals("/<")) {
				Edge edgeC = edge.deepCopy();
				for (Category c : edge.getCombinedAnteList()) {
					if (c.getName().equals("//")) continue;
					if (c.getFeatureMap().canUnify(edge.getNextActive().getFeatureMap())) {
						edgeC = null;
						break;
					}
				}
				if (edgeC != null) {
					newEdges.add(edgeC);
				}
			} else {
				continue;
			}
			
			if (newEdges.isEmpty()) {
				if (debug) log("CANNOT RESOLVE: " + edge + "\n");
			}
			for (Edge newEdge : newEdges) {
				newEdge.step();
				boolean isNewEdge = chart.addEdge(newEdge);
				if (debug) log("RESOLVE (" + (isNewEdge ? "NEW" : "KNOWN") + "): " + edge + "  --->  " + newEdge + "\n");
			}
		}
		progressTable.put("resolution", l1.size());
	}
	
	private static List<Category> copyExceptionsList(List<Category> list) {
		List<Category> listCopy = new ArrayList<Category>();
		for (Category x : list) {
			listCopy.add(x.deepCopy());
		}
		return listCopy;
	}
	
	private void log(String text) {
		System.err.print(text);
	}

}