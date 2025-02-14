import java.io.*;
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
            //     System.out.println(candidate);
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
        // ainda falta colocar o . nos numero (7.256 votos)
        // e voltar com o * dos nomes q tem asterisco
        System.out.println("\nVereadores eleitos:");

        
        List<Candidate> electedCandidates = new ArrayList<Candidate>();

        for (Candidate candidate : candidates.values()) {
            if (candidate.getCandidateSituation() == CandidateSituation.ELECTED) {
                electedCandidates.add(candidate);
            }
        }

        Collections.sort(electedCandidates, (c1, c2) -> Integer.compare(c2.getCandidateVotes(), c1.getCandidateVotes()));

        int counter = 0;

        for (Candidate candidate : electedCandidates) {
            counter++;
            System.out.print(counter + " - ");
            System.out.println(candidate.getCandidateName() + " (" + candidate.getPartyAcronym() + ", " + candidate.getCandidateVotes() + " votos)");
        }       
    }
}
