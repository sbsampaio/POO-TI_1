import java.io.*;
import java.text.NumberFormat;
import java.util.*;

// import java.util.HashMap;

public class ElectionReport {
    private CandidateFileReader candidateData;
    private ElectionFileReader votingData;
    private HashMap<Integer, Candidate> candidates;

    public ElectionReport(CandidateFileReader candidateData, ElectionFileReader votingData) {
        try {
            this.candidateData = candidateData;
            this.votingData = votingData;
            this.candidates = Candidate.CreateCandidates(candidateData.getValues());

            votingData.ComputeVotes(this.candidates);

            // for (Candidate candidate : this.candidates.values()) {
            // System.out.println(candidate);
            // }
            getQuantityElected();
            getElectedCandidates();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getQuantityElected() {
        int quantity = 0;

        for (Candidate candidate : candidates.values()) {
            if (candidate.getCandidateSituation() == CandidateSituation.ELECTED) {
                quantity++;
            }
        }

        System.out.println("Número de vagas: " + quantity);
    }

    public void getElectedCandidates() {
        System.out.println("\nVereadores eleitos:");

        List<Candidate> electedCandidates = new ArrayList<Candidate>();

        for (Candidate candidate : candidates.values()) {
            if (candidate.getCandidateSituation() == CandidateSituation.ELECTED) {
                electedCandidates.add(candidate);
            }
        }

        // order by votes (decrescent)
        Collections.sort(electedCandidates,
                (c1, c2) -> Integer.compare(c2.getCandidateVotes(), c1.getCandidateVotes()));
        // em caso de empate, os mais velhos tem prioridade

        int counter = 0;

        for (Candidate candidate : electedCandidates) {
            NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
            
            counter++;
            System.out.print(counter + " - ");
            // if the candidate is part of a federation, print an asterisk
            if (candidate.getFederationNumber() != -1) {
                System.out.print("*");
            }
            System.out.println(candidate.getCandidateName() + " (" + candidate.getPartyAcronym() + ", "
                    + brFormat.format(candidate.getCandidateVotes()) + " votos)");
        }
    }
}
