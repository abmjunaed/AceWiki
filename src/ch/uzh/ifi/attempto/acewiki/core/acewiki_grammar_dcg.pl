% This code is automatically generated on the basis of a file in Codeco notation.
%
% For more information, see the package ch.uzh.ifi.attempto.codeco of the AceWiki system
% (http://attempto.ifi.uzh.ch/acewiki/) and the thesis "Controlled English for Knowledge
% Representation" (http://attempto.ifi.uzh.ch/site/pubs/papers/doctoral_thesis_kuhn.pdf).


/* === AceWiki Grammar === */
/* - Tobias Kuhn, 8 December 2010 - */
/* Below, the grammar rules of the AceWiki grammar are shown: */

/* --- Texts and Sentences --- */
/* 'text' stands for a complete text consisting of an arbitrary number of complete
		sentences (including zero): */
text([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(text, []), Z/Z)-->[].
text([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(text, (Z, A1)), B1/C1)-->complete_sentence([D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2], Z, B1/C2), text([D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, Y2, Z2, A3, B3], A1, C2/C1).
/* A complete sentence is represented by the category 'complete_sentence' and is either
		a declarative sentence that ends with a full stop or a question ending with a question mark: */
complete_sentence([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(complete_sentence, (//, Z, ['.'])), A1/B1)-->[C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2]// (A1/B2), sentence([C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, Y2, Z2, A3], Z, B2/B3), ['.'], ~(A1/B3/B1).
complete_sentence([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(complete_sentence, (//, Z, [?])), A1/B1)-->[C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2]// (A1/B2), simple_sentence_2([plus, minus, plus, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2], Z, B2/Y2), [?], ~(A1/Y2/B1).
/* General sentences are represented by 'sentence': */
sentence([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(sentence, Z), A1/B1)-->sentence_coord_1([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2], Z, A1/B1).
sentence([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(sentence, (//, ['for every'], Z, A1)), B1/C1)-->[D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2]// (B1/C2), ['for every'], nc([minus, D2, E2, minus, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, Y2, Z2], Z, C2/A3), sentence_coord_1([B3, C3, D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3, O3, P3, Q3, R3, S3, T3, U3, V3, W3, X3, Y3, Z3], A1, A3/A4), ~(B1/A4/C1).
sentence([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(sentence, (//, [if], Z, [then], A1)), B1/C1)-->[D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2]// (B1/C2), [if], sentence_coord_1([D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, Y2, Z2, A3, B3], Z, C2/C3), [then], sentence_coord_1([D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3, O3, P3, Q3, R3, S3, T3, U3, V3, W3, X3, Y3, Z3, A4, B4], A1, C3/C4), ~(B1/C4/C1).
/* Sentences can be coordinated using "or" ('sentence_coord_1') and "and"
		('sentence_coord_2'): */
sentence_coord_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(sentence_coord_1, Z), A1/B1)-->sentence_coord_2([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2], Z, A1/B1).
sentence_coord_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(sentence_coord_1, (//, Z, [or], A1)), B1/C1)-->[D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2]// (B1/C2), sentence_coord_2([D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, Y2, Z2, A3, B3], Z, C2/C3), [or], sentence_coord_1([D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3, O3, P3, Q3, R3, S3, T3, U3, V3, W3, X3, Y3, Z3, A4, B4], A1, C3/C4), ~(B1/C4/C1).
sentence_coord_2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(sentence_coord_2, Z), A1/B1)-->simple_sentence_1([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2], Z, A1/B1).
sentence_coord_2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(sentence_coord_2, (Z, [and], A1)), B1/C1)-->simple_sentence_1([D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2], Z, B1/C2), [and], sentence_coord_2([D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, Y2, Z2, A3, B3], A1, C2/C1).
/* Uncoordinated sentences are represented in two levels by 'simple_sentence_1' and
		'simple_sentence_2': */
simple_sentence_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(simple_sentence_1, (//, ['it is false that'], Z)), A1/B1)-->[C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2]// (A1/B2), ['it is false that'], simple_sentence_2([minus, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, Y2, Z2], Z, B2/A3), ~(A1/A3/B1).
simple_sentence_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(simple_sentence_1, (['there is'], Z)), A1/B1)-->['there is'], np([minus, C1, D1, minus, plus, minus, minus, nom, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1], Z, A1/B1).
simple_sentence_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(simple_sentence_1, (['there is'], Z, ['such that'], A1)), B1/C1)-->['there is'], np([minus, D1, E1, minus, plus, minus, minus, nom, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1], Z, B1/W1), ['such that'], simple_sentence_1([X1, Y1, Z1, A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2], A1, W1/C1).
simple_sentence_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(simple_sentence_1, (['there are'], Z)), A1/B1)-->['there are'], np([minus, C1, D1, minus, plus, minus, plus, nom, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1], Z, A1/B1).
simple_sentence_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(simple_sentence_1, Z), A1/B1)-->simple_sentence_2([minus, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1], Z, A1/B1).
simple_sentence_2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(simple_sentence_2(qu:A, whin:B, whout:C), (Z, A1)), B1/C1)-->np([A, B, D1, minus, E1, F1, G1, nom, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1], Z, B1/Y1), vp_coord_1([A, D1, C, H1, Z1, A2, G1, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2], A1, Y1/T2), ~(B1/T2/C1).

/* --- Verb Phrases --- */
/* Like sentences, verb phrases can be coordinated using "or" ('vp_coord_1') and "and"
		('vp_coord_2'): */
vp_coord_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(vp_coord_1(subj:D, pl:G, qu:A, whin:B, whout:C), Z), A1/B1)-->vp_coord_2([A, B, C, D, C1, D1, G, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1], Z, A1/B1).
vp_coord_1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(vp_coord_1(subj:D, pl:G, qu:A, whin:B, whout:C), (//, Z, [or], A1)), B1/C1)-->[D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2]// (B1/C2), vp_coord_2([A, B, D2, D, E2, F2, G, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2], Z, C2/Y2), [or], vp_coord_1([A, D2, C, D, Z2, A3, G, B3, C3, D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3, O3, P3, Q3, R3, S3], A1, Y2/T3), ~(B1/T3/C1).
vp_coord_2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(vp_coord_2(subj:D, pl:G, qu:A, whin:B, whout:C), Z), A1/B1)-->vp([A, B, C, D, C1, D1, G, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1], Z, A1/B1).
vp_coord_2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(vp_coord_2(subj:D, pl:G, qu:A, whin:B, whout:C), (Z, [and], A1)), B1/C1)-->vp([A, B, D1, D, E1, F1, G, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1], Z, B1/Y1), [and], vp_coord_2([A, D1, C, D, Z1, A2, G, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2], A1, Y1/C1).
/* Uncoordinated verb phrases represented by 'vp' can use an auxiliary verb: */
vp([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(vp(subj:D, exist:E, rel:J, pl:G, qu:A, whin:B, whout:C), (Z, A1)), B1/C1)-->aux([D1, E1, F1, G1, E, H1, G, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1], Z, B1/A2), v([A, B, C, D, E, B2, G, C2, D2, J, L1, inf, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2], A1, A2/R2), ~(B1/R2/C1).
vp([A, B, C, D, plus, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X], =>(vp(subj:D, exist:plus, rel:I, pl:F, qu:A, whin:B, whout:C), Y), Z/A1)-->v([A, B, C, D, plus, B1, F, C1, D1, I, minus, fin, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1], Y, Z/R1), ~(Z/R1/A1).
/* The category 'v' represents the main verb or - if "be" is used as a copula verb - the
		complementing noun phrase or adjective complement: */
v([A, B, B, C, D, E, F, G, H, I, minus, J, minus, K, L, M, N, O, P, Q, R, S, T, U, V], =>(v(be:minus, exist:D, pl:F, vform:J, copula:minus, whin:B, whout:B), W), X/Y)-->verb([Z, A1, B1, C1, D, D1, F, E1, F1, G1, minus, J, H1, itr, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1], W, X/Y).
v([A, B, C, D, E, F, G, H, I, J, minus, K, minus, L, M, N, O, P, Q, R, S, T, U, V, W], =>(v(subj:D, be:minus, exist:E, rel:J, pl:G, vform:K, embv:M, copula:minus, qu:A, whin:B, whout:C), (X, Y)), Z/A1)-->verb([B1, C1, D1, E1, E, F1, G, G1, H1, I1, minus, K, J1, tr, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1], X, Z/V1), np([A, B, C, D, W1, X1, Y1, acc, Z1, J, A2, B2, C2, tr, M, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2], Y, V1/A1).
v([A, B, C, D, E, F, G, H, I, J, plus, K, minus, L, M, N, O, P, Q, R, S, T, U, V, W], =>(v(subj:D, be:plus, rel:J, embv:M, copula:minus, qu:A, whin:B, whout:C), (X, Y)), Z/A1)-->verb([B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, plus, L1, M1, tr, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1], X, Z/Y1), np([A, B, C, D, Z1, A2, B2, acc, C2, J, D2, E2, minus, F2, M, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2], Y, Y1/A1).
v([A, B, C, D, E, F, G, H, I, J, plus, K, plus, L, M, N, O, P, Q, R, S, T, U, V, W], =>(v(subj:D, be:plus, rel:J, embv:M, copula:plus, qu:A, whin:B, whout:C), X), Y/Z)-->np([A, B, C, D, A1, B1, minus, acc, C1, J, D1, E1, plus, F1, M, plus, G1, H1, I1, J1, K1, L1, M1, N1, O1], X, Y/Z).
v([A, B, C, D, E, F, minus, G, H, I, plus, J, plus, K, L, M, N, O, P, Q, R, S, T, U, V], =>(v(subj:D, be:plus, rel:I, pl:minus, embv:L, copula:plus, qu:A, whin:B, whout:C), W), X/Y)-->np([A, B, C, D, Z, A1, minus, acc, B1, I, C1, D1, plus, E1, L, minus, F1, G1, H1, I1, J1, K1, L1, M1, N1], W, X/Y).
v([A, B, C, D, E, F, G, H, I, J, plus, K, plus, L, M, N, O, P, Q, R, S, T, U, V, W], =>(v(subj:D, be:plus, rel:J, embv:M, copula:plus, qu:A, whin:B, whout:C), (X, Y)), Z/A1)--> $tradj([B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1], X, Z/Z), np([A, B, C, D, A2, B2, C2, acc, D2, J, E2, F2, minus, G2, M, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2], Y, Z/A1).

/* --- Noun Phrases --- */
/* Noun phrases are represented by 'np' and can consist of proper names, variables,
		pronouns, and different noun constructs: */
np([A, B, C, D, plus, plus, minus, E, F, G, H, I, J, K, L, minus, M, N, O, P, Q, R, S, T, U], =>(np(id:F, exist:plus, rel:G, of:minus, def:plus, pl:minus, embv:L, qu:A, whin:B, whout:C), (V, >>(id:F, human:W, gender:X, type:prop, hasvar:minus), Y)), Z/A1)--> $propername([B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, W, X, F, R1, S1, T1, U1, V1, W1], V, Z/Z), [X1, Y1, Z1, A2, B2, C2, D2, E2, F, F2, G2, H2, I2, J2, K2, L2, W, X, M2, prop, minus, N2, O2, P2, Q2]>> (Z/R2), relcl([A, B, C, F, S2, T2, U2, V2, W2, G, X2, Y2, Z2, A3, L, B3, W, C3, D3, E3, F3, G3, H3, I3, J3], Y, R2/A1).
np([A, B, B, C, plus, plus, minus, D, E, F, G, H, I, J, K, minus, L, M, N, O, P, Q, R, S, T], =>(np(id:E, exist:plus, of:minus, def:plus, pl:minus, whin:B, whout:B), (#(E), U, >(id:E, type:var, hasvar:plus, var:V))), W/X)--> #(E), newvar([Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, V, T1, U1, V1], U, W/W1), [X1, Y1, Z1, A2, B2, C2, D2, E2, E, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, var, plus, V, P2, Q2, R2]>W1/X.
np([A, B, B, C, plus, plus, minus, D, E, F, G, H, I, J, K, minus, L, M, N, O, P, Q, R, S, T], =>(np(id:E, exist:plus, of:minus, def:plus, pl:minus, whin:B, whout:B), (U, V, <(id:E, type:noun, hasvar:plus, noun:W, var:X, human:Y, gender:Z), >(id:E, human:Y, gender:Z, type:ref, hasvar:minus))), A1/B1)--> $defnoun([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, W, Y1, Z1], U, A1/A1), $reference([A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, X, S2, T2, U2, V2, W2, X2], V, A1/A1), [+[Y2, Z2, A3, B3, C3, D3, E3, F3, E, G3, H3, I3, J3, K3, L3, M3, Y, Z, N3, noun, plus, X, W, O3, P3]]<A1/Q3, [R3, S3, T3, U3, V3, W3, X3, Y3, E, Z3, A4, B4, C4, D4, E4, F4, Y, Z, G4, ref, minus, H4, I4, J4, K4]>Q3/B1.
np([A, B, B, C, plus, plus, minus, D, E, F, G, H, I, J, K, minus, L, M, N, O, P, Q, R, S, T], =>(np(id:E, exist:plus, of:minus, def:plus, pl:minus, whin:B, whout:B), (U, <(id:E, type:noun, noun:V, human:W, gender:X), >(id:E, human:W, gender:X, type:ref, hasvar:minus))), Y/Z)--> $defnoun([A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, V, W1, X1], U, Y/Y), [+[Y1, Z1, A2, B2, C2, D2, E2, F2, E, G2, H2, I2, J2, K2, L2, M2, W, X, N2, noun, O2, P2, V, Q2, R2]]<Y/S2, [T2, U2, V2, W2, X2, Y2, Z2, A3, E, B3, C3, D3, E3, F3, G3, H3, W, X, I3, ref, minus, J3, K3, L3, M3]>S2/Z.
np([A, B, B, C, plus, plus, minus, D, E, F, G, H, I, J, K, minus, L, M, N, O, P, Q, R, S, T], =>(np(id:E, exist:plus, of:minus, def:plus, pl:minus, whin:B, whout:B), (U, <(id:E, hasvar:plus, var:V, human:W, gender:X), >(id:E, human:W, gender:X, type:ref, hasvar:minus))), Y/Z)--> $reference([A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, V, S1, T1, U1, V1, W1, X1], U, Y/Y), [+[Y1, Z1, A2, B2, C2, D2, E2, F2, E, G2, H2, I2, J2, K2, L2, M2, W, X, N2, O2, plus, V, P2, Q2, R2]]<Y/S2, [T2, U2, V2, W2, X2, Y2, Z2, A3, E, B3, C3, D3, E3, F3, G3, H3, W, X, I3, ref, minus, J3, K3, L3, M3]>S2/Z.
np([A, B, C, D, E, F, minus, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X], =>(np(id:H, subj:D, exist:E, rel:I, of:O, pl:minus, embv:N, qu:A, whin:B, whout:C), (Y, Z)), A1/B1)-->quant([A, C1, D1, E1, E, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1], Y, A1/Z1), nc([A, B, C, D, A2, B2, C2, D2, H, I, E2, F2, G2, H2, N, O, I2, J2, K2, L2, M2, N2, O2, P2, Q2], Z, Z1/B1).
np([A, B, C, D, E, F, minus, G, H, I, J, K, L, M, N, minus, O, P, Q, R, S, T, U, V, W], =>(np(id:H, exist:E, rel:I, of:minus, pl:minus, embv:N, qu:A, whin:B, whout:C), (#(H), X, Y, >(id:H, human:Z, type:ipron, hasvar:A1, var:B1), C1)), D1/E1)--> #(H), ipron([A, F1, G1, H1, E, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, Z, T1, U1, V1, W1, X1, Y1, Z1, A2], X, D1/B2), opt_newvar([C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, A1, B1, W2, X2, Y2], Y, B2/Z2), [A3, B3, C3, D3, E3, F3, G3, H3, H, I3, J3, K3, L3, M3, N3, O3, Z, P3, Q3, ipron, A1, B1, R3, S3, T3]>Z2/U3, relcl([A, B, C, H, V3, W3, X3, Y3, Z3, I, A4, B4, C4, D4, N, E4, Z, F4, G4, H4, I4, J4, K4, L4, M4], C1, U3/E1).
np([A, B, B, C, plus, D, plus, E, F, G, H, I, minus, J, K, minus, L, M, N, O, P, Q, R, S, T], =>(np(id:F, exist:plus, of:minus, pl:plus, copula:minus, whin:B, whout:B), (U, V, #(F), W)), X/Y)-->num_quant([Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1], U, X/Y), $number([Y1, Z1, A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2], V, Y/Y), #(F), $nounpl([X2, Y2, Z2, A3, B3, C3, D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3, O3, P3, Q3, R3, S3, T3, U3, V3], W, Y/Y).
np([A, B, B, C, plus, D, minus, E, F, G, H, I, minus, J, K, minus, L, M, N, O, P, Q, R, S, T], =>(np(id:F, exist:plus, of:minus, pl:minus, copula:minus, whin:B, whout:B), (U, ['1'], #(F), V, >(id:F, human:W, gender:X, type:noun, hasvar:minus, noun:Y))), Z/A1)-->num_quant([B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1], U, Z/A2), ['1'], #(F), $noun([B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, W, X, Y, R2, S2, T2, U2, V2, W2], V, A2/A2), [X2, Y2, Z2, A3, B3, C3, D3, E3, F, F3, G3, H3, I3, J3, K3, L3, W, X, M3, noun, minus, N3, Y, O3, P3]>A2/A1.
np([plus, minus, plus, A, plus, B, minus, C, D, E, F, G, H, I, J, minus, K, L, M, N, O, P, Q, R, S], =>(np(id:D, exist:plus, of:minus, pl:minus, qu:plus, whin:minus, whout:plus), (#(D), [what], >(id:D, human:minus, type:wh, hasvar:minus))), T/U)--> #(D), [what], [V, W, X, Y, Z, A1, B1, C1, D, D1, E1, F1, G1, H1, I1, J1, minus, K1, L1, wh, minus, M1, N1, O1, P1]>T/U.
np([plus, minus, plus, A, plus, B, minus, C, D, E, F, G, H, I, J, minus, K, L, M, N, O, P, Q, R, S], =>(np(id:D, exist:plus, of:minus, pl:minus, qu:plus, whin:minus, whout:plus), (#(D), [who], >(id:D, human:plus, type:wh, hasvar:minus))), T/U)--> #(D), [who], [V, W, X, Y, Z, A1, B1, C1, D, D1, E1, F1, G1, H1, I1, J1, plus, K1, L1, wh, minus, M1, N1, O1, P1]>T/U.
np([plus, minus, plus, A, plus, B, minus, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], =>(np(id:D, subj:A, exist:plus, rel:E, of:K, embv:J, pl:minus, qu:plus, whin:minus, whout:plus), ([which], U)), V/W)-->[which], nc([plus, plus, plus, A, X, Y, Z, A1, D, E, B1, C1, D1, E1, J, K, F1, G1, H1, I1, J1, K1, L1, M1, N1], U, V/W).
np([plus, minus, plus, A, plus, B, plus, C, D, E, F, G, H, I, J, minus, K, L, M, N, O, P, Q, R, S], =>(np(id:D, exist:plus, of:minus, pl:plus, qu:plus, whin:minus, whout:plus), ([which], #(D), T)), U/U)-->[which], #(D), $nounpl([V, W, X, Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1], T, U/U).
/* The category 'nc' represents nouns optionally followed by variables, relative clauses,
		and of-constructs: */
nc([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, minus, P, Q, R, S, T, U, V, W, X], =>(nc(id:I, rel:J, of:minus, embv:O, qu:A, whin:B, whout:C), (Y, Z, >(id:I, human:A1, gender:B1, type:noun, hasvar:C1, noun:D1, var:E1), F1)), G1/H1)-->n([I1, J1, K1, L1, M1, N1, O1, P1, I, Q1, R1, S1, T1, U1, V1, W1, A1, B1, D1, X1, Y1, Z1, A2, B2, C2], Y, G1/D2), opt_newvar([E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, X2, C1, E1, Y2, Z2, A3], Z, D2/B3), [C3, D3, E3, F3, G3, H3, I3, J3, I, K3, L3, M3, N3, O3, P3, Q3, A1, B1, R3, noun, C1, E1, D1, S3, T3]>B3/U3, relcl([A, B, C, I, V3, W3, X3, Y3, Z3, J, A4, B4, C4, D4, O, E4, A1, F4, G4, H4, I4, J4, K4, L4, M4], F1, U3/H1).
nc([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, plus, P, Q, R, S, T, U, V, W, X], =>(nc(subj:D, rel:J, of:plus, embv:O, qu:A, whin:B, whout:C), (Y, Z)), A1/B1)--> $nounof([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2], Y, A1/A1), np([A, B, C, D, B2, C2, D2, acc, E2, J, F2, G2, H2, I2, O, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2], Z, A1/T2), ~(A1/T2/B1).
/* The category 'n' stands for nouns: */
n([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(n(id:I, human:Q, gender:R, text:S), (#(I), Z)), A1/A1)--> #(I), $noun([B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, Q, R, S, R1, S1, T1, U1, V1, W1], Z, A1/A1).
/* New variables, optional and mandatory, are represented by 'opt_newvar' and 'newvar',
		respectively: */
opt_newvar([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, minus, U, V, W, X], =>(opt_newvar(hasvar:minus), []), Y/Y)-->[].
opt_newvar([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, plus, U, V, W, X], =>(opt_newvar(hasvar:plus, var:U), Y), Z/A1)-->newvar([B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, U, W1, X1, Y1], Y, Z/A1).
newvar([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(newvar(var:V), (Z, /<(hasvar:plus, var:V))), A1/B1)--> $variable([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, V, U1, V1, W1, X1, Y1, Z1], Z, A1/A1), /<([A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, plus, V, U2, V2, W2], A1/B1).

/* --- Relative Clauses --- */
/* Relative clauses are represented by 'relcl'. They start with a relative pronoun and
		are always optional: */
relcl([A, B, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X], =>(relcl(whin:B, whout:B), []), Y/Y)-->[].
relcl([A, B, C, D, E, F, G, H, I, plus, J, K, L, M, plus, N, O, P, Q, R, S, T, U, V, W], =>(relcl(subj:D, rel:plus, embv:plus, human:O, qu:A, whin:B, whout:C), (X, Y)), Z/A1)-->relpron([B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, O, R1, S1, T1, U1, V1, W1, X1, Y1], X, Z/Z1), relcl1([A, B, C, D, A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, O, M2, N2, O2, P2, Q2, R2, X1, S2], Y, Z1/A1).
/* Like sentences and verb phrases, relative clauses can be coordinated by "or"
		('relcl1') and "and" ('relcl2'): */
relcl1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(relcl1(subj:D, human:Q, relpron:X, qu:A, whin:B, whout:C), (//, Z, A1, B1)), C1/D1)-->[E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2, C2]// (C1/D2), relcl2([A, B, E2, D, F2, G2, H2, I2, J2, minus, K2, L2, M2, N2, O2, P2, Q, Q2, R2, S2, T2, U2, V2, X, W2], Z, D2/X2), or_relpron([Y2, Z2, A3, B3, C3, D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3, Q, O3, P3, Q3, R3, S3, T3, X, U3], A1, X2/V3), relcl1([A, E2, C, D, W3, X3, Y3, Z3, A4, B4, C4, D4, E4, F4, G4, H4, Q, I4, J4, K4, L4, M4, N4, X, O4], B1, V3/P4), ~(C1/P4/D1).
relcl1([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(relcl1(subj:D, human:Q, relpron:X, qu:A, whin:B, whout:C), Z), A1/B1)-->relcl2([A, B, C, D, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, Q, O1, P1, Q1, R1, S1, T1, X, U1], Z, A1/B1).
relcl2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(relcl2(subj:D, rel:J, relpron:X, human:Q, qu:A, whin:B, whout:C), (Z, A1, B1)), C1/D1)-->vp([A, B, E1, D, F1, G1, minus, H1, I1, minus, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1], Z, C1/Y1), and_relpron([Z1, A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, Q, P2, Q2, R2, S2, T2, U2, X, V2], A1, Y1/W2), relcl2([A, E1, C, D, X2, Y2, Z2, A3, B3, J, C3, D3, E3, F3, G3, H3, Q, I3, J3, K3, L3, M3, N3, X, O3], B1, W2/D1).
relcl2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(relcl2(subj:D, rel:J, qu:A, whin:B, whout:C), Z), A1/B1)-->vp([A, B, C, D, C1, D1, minus, E1, F1, J, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1], Z, A1/B1).
relcl2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(relcl2(subj:D, qu:A, whin:B, whout:C), (Z, A1, B1)), C1/D1)-->np([A, B, C, D, E1, F1, G1, nom, H1, minus, I1, J1, minus, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, minus], Z, C1/V1), aux([W1, X1, Y1, Z1, A2, B2, G1, C2, D2, E2, minus, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2], A1, V1/T2), verb([U2, V2, W2, X2, A2, Y2, G1, Z2, A3, B3, minus, inf, C3, tr, D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3], B1, T2/O3), ~(C1/O3/D1).
relcl2([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(relcl2(subj:D, qu:A, whin:B, whout:C), (Z, A1)), B1/C1)-->np([A, B, C, D, D1, E1, F1, nom, G1, minus, H1, I1, minus, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, minus], Z, B1/U1), verb([V1, W1, X1, Y1, plus, Z1, F1, A2, B2, C2, minus, fin, D2, tr, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2], A1, U1/P2), ~(B1/P2/C1).
/* Relative pronouns are represented by 'relpron' and can be either "that", "who" or
		"which": */
relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, that, X], =>(relpron(relpron:that), [that]), Y/Y)-->[that].
relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, plus, Q, R, S, T, U, V, who, W], =>(relpron(human:plus, relpron:who), [who]), X/X)-->[who].
relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, minus, Q, R, S, T, U, V, which, W], =>(relpron(human:minus, relpron:which), [which]), X/X)-->[which].
/* The categories 'or_relpron' and 'and_relpron' define shortcuts - like "or that" as
		one token - for better usability inside of the predictive editor: */
or_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(or_relpron(human:Q, relpron:X), ([or], Z)), A1/B1)-->[or], relpron([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, Q, S1, T1, U1, V1, W1, X1, X, Y1], Z, A1/B1).
or_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, that, X], =>(or_relpron(relpron:that), ['or that']), Y/Y)-->['or that'].
or_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, plus, Q, R, S, T, U, V, who, W], =>(or_relpron(human:plus, relpron:who), ['or who']), X/X)-->['or who'].
or_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, minus, Q, R, S, T, U, V, which, W], =>(or_relpron(human:minus, relpron:which), ['or which']), X/X)-->['or which'].
and_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(and_relpron(human:Q, relpron:X), ([and], Z)), A1/B1)-->[and], relpron([C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, Q, S1, T1, U1, V1, W1, X1, X, Y1], Z, A1/B1).
and_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, that, X], =>(and_relpron(relpron:that), ['and that']), Y/Y)-->['and that'].
and_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, plus, Q, R, S, T, U, V, who, W], =>(and_relpron(human:plus, relpron:who), ['and who']), X/X)-->['and who'].
and_relpron([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, minus, Q, R, S, T, U, V, which, W], =>(and_relpron(human:minus, relpron:which), ['and which']), X/X)-->['and which'].

/* --- Verbs --- */
/* The category 'verb' represents main verbs: */
verb([A, B, C, D, E, F, minus, G, H, I, minus, fin, J, tr, K, L, M, N, O, P, Q, R, S, T, U], =>(verb(be:minus, vcat:tr, pl:minus, vform:fin), V), W/W)--> $verbsg([X, Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1], V, W/W).
verb([A, B, C, D, E, F, plus, G, H, I, minus, fin, J, tr, K, L, M, N, O, P, Q, R, S, T, U], =>(verb(be:minus, vcat:tr, pl:plus, vform:fin), V), W/W)--> $verbinf([X, Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1], V, W/W).
verb([A, B, C, D, E, F, G, H, I, J, minus, inf, K, tr, L, M, N, O, P, Q, R, S, T, U, V], =>(verb(be:minus, vcat:tr, vform:inf), W), X/X)--> $verbinf([Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1], W, X/X).
verb([A, B, C, D, E, F, G, H, I, J, plus, K, L, tr, M, N, O, P, Q, R, S, T, U, V, W], =>(verb(be:plus, vcat:tr), X), Y/Y)--> $pverb([Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1], X, Y/Y).
/* Auxiliary verbs are represented by 'aux', which includes negation markers: */
aux([A, B, C, D, plus, E, minus, F, G, H, plus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:plus, exist:plus, pl:minus), [is]), W/W)-->[is].
aux([A, B, C, D, minus, E, minus, F, G, H, plus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:plus, exist:minus, pl:minus), (//, ['is not'])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), ['is not'].
aux([A, B, C, D, minus, E, minus, F, G, H, plus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:plus, exist:minus, pl:minus), (//, [is, not])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), [is, not].
aux([A, B, C, D, plus, E, plus, F, G, H, plus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:plus, exist:plus, pl:plus), [are]), W/W)-->[are].
aux([A, B, C, D, minus, E, plus, F, G, H, plus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:plus, exist:minus, pl:plus), (//, ['are not'])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), ['are not'].
aux([A, B, C, D, minus, E, plus, F, G, H, plus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:plus, exist:minus, pl:plus), (//, [are, not])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), [are, not].
aux([A, B, C, D, minus, E, minus, F, G, H, minus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:minus, exist:minus, pl:minus), (//, ['does not'])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), ['does not'].
aux([A, B, C, D, minus, E, plus, F, G, H, minus, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], =>(aux(be:minus, exist:minus, pl:plus), (//, ['do not'])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), ['do not'].

/* --- Quantifiers --- */
/* Existential and universal quantifiers are represented by 'quant': */
quant([A, B, C, D, plus, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X], =>(quant(exist:plus), [a]), Y/Y)-->[a].
quant([A, B, C, D, plus, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X], =>(quant(exist:plus), [an]), Y/Y)-->[an].
quant([minus, A, B, C, minus, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W], =>(quant(exist:minus, qu:minus), (//, [every])), X/Y)-->[Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1]// (X/Y), [every].
quant([A, B, C, D, minus, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X], =>(quant(exist:minus), (//, [no])), Y/Z)-->[A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1]// (Y/Z), [no].
/* The category 'num_quant' stands for numerical quantifiers: */
num_quant([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(num_quant, ['at least']), Z/Z)-->['at least'].
num_quant([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(num_quant, ['at most']), Z/Z)-->['at most'].
num_quant([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(num_quant, ['less than']), Z/Z)-->['less than'].
num_quant([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(num_quant, ['more than']), Z/Z)-->['more than'].
num_quant([A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y], =>(num_quant, [exactly]), Z/Z)-->[exactly].

/* --- Indefinite Pronouns --- */
/* Indefinite pronouns are represented by 'ipron': */
ipron([A, B, C, D, plus, E, F, G, H, I, J, K, L, M, N, O, minus, P, Q, R, S, T, U, V, W], =>(ipron(exist:plus, human:minus), [something]), X/X)-->[something].
ipron([A, B, C, D, plus, E, F, G, H, I, J, K, L, M, N, O, plus, P, Q, R, S, T, U, V, W], =>(ipron(exist:plus, human:plus), [somebody]), X/X)-->[somebody].
ipron([minus, A, B, C, minus, D, E, F, G, H, I, J, K, L, M, N, minus, O, P, Q, R, S, T, U, V], =>(ipron(exist:minus, human:minus, qu:minus), (//, [everything])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), [everything].
ipron([minus, A, B, C, minus, D, E, F, G, H, I, J, K, L, M, N, plus, O, P, Q, R, S, T, U, V], =>(ipron(exist:minus, human:plus, qu:minus), (//, [everybody])), W/X)-->[Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1]// (W/X), [everybody].
ipron([A, B, C, D, minus, E, F, G, H, I, J, K, L, M, N, O, minus, P, Q, R, S, T, U, V, W], =>(ipron(exist:minus, human:minus), (//, [nothing])), X/Y)-->[Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1]// (X/Y), [nothing].
ipron([A, B, C, D, minus, E, F, G, H, I, J, K, L, M, N, O, plus, P, Q, R, S, T, U, V, W], =>(ipron(exist:minus, human:plus), (//, [nobody])), X/Y)-->[Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1]// (X/Y), [nobody].


~(I/T/O) --> {append([X,[//|N],I],T), \+ member(//,N), findall(>>(R),member(>>(R),X),Y), append([Y,N,I],O)}, !.
~(_/O/O) --> [].
//(_, T/[//|T]) --> [].
>(F, T/[>(F)|T]) --> [].
>>(F, T/[>>(F)|T]) --> [].
<(L, [R|T]/[R|T]) --> {R =.. [_,Q], \+ member(-Q, L), \+ \+ member(+Q, L), !, member(+Q, L)}.
<(L, [R|T]/[R|T]) --> <(L,T/T).
/<(F, T/T) --> {\+ (member(R,T), R =.. [_,F])}, !.
#(#(P),L,L) :- length(L,P).
