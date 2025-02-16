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

            printQuantityElected();
            printElectedCandidates();
            printMostVotedCandidates();
            printMajorityElectedCandidates();
            printElectedWithProportionalBenefit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printQuantityElected() {
        System.out.println("Número de vagas: " + getQuantityElected());
    }

    /*
     * return the quantity of elected candidates
     */
    public int getQuantityElected() {
        int quantity = 0;

        for (Candidate candidate : candidates.values()) {
            if (candidate.getCandidateSituation() == CandidateSituation.ELECTED) {
                quantity++;
            }
        }

        return quantity;
    }

    /*
     * return the elected candidates
     */
    public List<Candidate> getElectedCandidates() {
        List<Candidate> electedCandidates = new ArrayList<Candidate>();

        for (Candidate candidate : candidates.values()) {
            if (candidate.getCandidateSituation() == CandidateSituation.ELECTED) {
                electedCandidates.add(candidate);
            }
        }

        // order by votes (decrescent)
        Collections.sort(electedCandidates,
                (c1, c2) -> Integer.compare(c2.getCandidateVotes(), c1.getCandidateVotes()));

        return electedCandidates;
    }

    /*
     * print the elected candidates
     */
    public void printElectedCandidates() {
        System.out.println("\nVereadores eleitos:");

        List<Candidate> electedCandidates = getElectedCandidates();

        printCandidatePartyVotes(electedCandidates);
    }

    /*
     * return the most voted candidates
     */
    public List<Candidate> getMostVotedCandidates(int limit) {
        List<Candidate> mostVotedCandidates = new ArrayList<>(candidates.values());
    
        // Ordena por número de votos em ordem decrescente
        mostVotedCandidates.sort((c1, c2) -> Integer.compare(c2.getCandidateVotes(), c1.getCandidateVotes()));
    
        // Retorna apenas os primeiros 'limit' candidatos, garantindo que não ultrapasse o tamanho da lista
        return mostVotedCandidates.subList(0, Math.min(limit, mostVotedCandidates.size()));
    }
    
    /*
     * print the most voted candidates
     */
    public void printMostVotedCandidates() {
        System.out.println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");

        List<Candidate> mostVotedCandidates = getMostVotedCandidates(getQuantityElected());

        printCandidatePartyVotes(mostVotedCandidates);
    }

    /*
     * print the candidates that would have been elected if the majority criterion 
     * had been used, but weren't
     */
    public void printMajorityElectedCandidates()
    {
        System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");

        List<Candidate> majorityElectedCandidates = getMostVotedCandidates(getQuantityElected());
        List<Candidate> electedCandidates = getElectedCandidates();

        for (Candidate candidate : electedCandidates) {
            majorityElectedCandidates.remove(candidate);
        }

        printCandidatePartyVotes(majorityElectedCandidates);

    }

    /*
     * print the candidates that were elected with benefited from the proportional system, but
     * wouldn't have been elected if the majority criterion had been used
     */
    public void printElectedWithProportionalBenefit()
    {
        System.out.println("\nEleitos, que se beneficiaram do sistema proporcional:\n" +
                        "(com sua posição no ranking de mais votados)");
        
        List<Candidate> electedCandidates = getElectedCandidates();
        List<Candidate> majorityElectedCandidates = getMostVotedCandidates(getQuantityElected());

        for (Candidate candidate : majorityElectedCandidates) {
            electedCandidates.remove(candidate);
        }

        printCandidatePartyVotes(electedCandidates);
    }

    /*
     * print the candidates name with the number of votes and the party acronym
     */
    public void printCandidatePartyVotes(List<Candidate> candidates) 
    {
        int counter = 0;
        for (Candidate candidate : candidates) {
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
