import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

// import java.util.HashMap;

public class ElectionReport {
    private CandidateFileReader candidateData;
    private ElectionFileReader votingData;
    private HashMap<Integer, Candidate> candidates;
    private HashMap<Integer, PoliticalParty> politicalParties;
    private LocalDate electionDate;

    public ElectionReport(CandidateFileReader candidateData, ElectionFileReader votingData, String candidateDataPath) {
        try {
            this.candidateData = candidateData;
            this.votingData = votingData;
            this.politicalParties = PoliticalParty.readPoliticalParties(candidateDataPath);
            this.candidates = Candidate.CreateCandidates(candidateData.getValues(), politicalParties);
            this.electionDate = votingData.getElectionDate();

            votingData.ComputeVotes(this.candidates, this.politicalParties);

            printQuantityElected();
            printElectedCandidates();
            printMostVotedCandidates();
            printMajorityElectedCandidates();
            printElectedWithProportionalBenefit();
            printPoliticalPartyData();
            printElectedByAge(electionDate);
            printElectedByGender();

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
        electedCandidates.sort(Comparator
                .comparing(Candidate::getCandidateVotes, Comparator.reverseOrder())
                .thenComparing(Candidate::getCandidateAge));

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

        mostVotedCandidates.sort(Comparator
                .comparing(Candidate::getCandidateVotes, Comparator.reverseOrder()));

        // Retorna apenas os primeiros 'limit' candidatos, garantindo que não ultrapasse
        // o tamanho da lista
        return mostVotedCandidates.subList(0, Math.min(limit, mostVotedCandidates.size()));
    }

    /*
     * print the most voted candidates
     */
    public void printMostVotedCandidates() {
        System.out
                .println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");

        List<Candidate> mostVotedCandidates = getMostVotedCandidates(getQuantityElected());

        printCandidatePartyVotes(mostVotedCandidates);
    }

    /*
     * print the candidates that would have been elected if the majority criterion
     * had been used, but weren't
     */
    public void printMajorityElectedCandidates() {
        System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");

        List<Candidate> majorityElectedCandidates = getMostVotedCandidates(getQuantityElected());
        List<Candidate> electedCandidates = getElectedCandidates();

        for (Candidate candidate : electedCandidates) {
            majorityElectedCandidates.remove(candidate);
        }

        printCandidatePartyVotes(majorityElectedCandidates);

    }

    /*
     * print the candidates that were elected with benefited from the proportional
     * system, but
     * wouldn't have been elected if the majority criterion had been used
     */
    public void printElectedWithProportionalBenefit() {
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
     * print all the political partys ordered by votes (decrescent)
     * shows the amount of nominal and legend votes, and the amount of
     * elected candidates
     */
    public void printPoliticalPartyData() {
        System.out.println("\nVotação dos partidos e número de candidatos eleitos:");

        List<PoliticalParty> politicalPartiesList = new ArrayList<>(politicalParties.values());

        politicalPartiesList.sort(Comparator
                .comparing(PoliticalParty::getTotalVotes, Comparator.reverseOrder())
                .thenComparing(PoliticalParty::getNumber));

        int counter = 0;
        for (PoliticalParty party : politicalPartiesList) {
            counter++;
            System.out.println(counter + " - " + party.getPartyAcronym() + " - " + party.getNumber() +
                    ", " + party.getTotalVotes() + " votos (" + party.getNominalVotes() + " nominais e " +
                    party.getLegendVotes() + " de legenda), " + party.getElectedCandidates() + " candidatos eleitos");
        }
    }

    public void printElectedByGender() {
        System.out.println("\nEleitos, por gênero:");

        List<Candidate> electedCandidates = getElectedCandidates();
        int feminine = 0, masculine = 0;

        for (Candidate candidate : electedCandidates) {
            if (candidate.getGender() == Gender.FEMININE) {
                feminine++;
            } else {
                masculine++;
            }
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        System.out.println(
                "Feminino: " + feminine + " (" + df.format((float) feminine / getQuantityElected() * 100) + "%)");
        System.out.println(
                "Masculino: " + masculine + " (" + df.format((float) masculine / getQuantityElected() * 100) + "%)");

    }

    public void printElectedByAge(LocalDate electionDate) {
        System.out.println("\nEleitos, por faixa etária (na data da eleição):");

        int faixa1 = 0, faixa2 = 0, faixa3 = 0, faixa4 = 0, faixa5 = 0;

        for (Candidate candidate : getElectedCandidates()) {
            candidate.setCandidateAge(electionDate);

            if (candidate.getCandidateAge() < 30) {
                faixa1++;
            } else if (candidate.getCandidateAge() < 40) {
                faixa2++;
            } else if (candidate.getCandidateAge() < 50) {
                faixa3++;
            } else if (candidate.getCandidateAge() < 60) {
                faixa4++;
            } else {
                faixa5++;
            }
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        System.out.println(
                "Idade < 30: " + faixa1 + " (" + df.format((float) faixa1 / getQuantityElected() * 100) + "%)");
        System.out.println(
                "30 <= Idade < 40: " + faixa2 + " (" + df.format((float) faixa2 / getQuantityElected() * 100) + "%)");
        System.out.println(
                "40 <= Idade < 50: " + faixa3 + " (" + df.format((float) faixa3 / getQuantityElected() * 100) + "%)");
        System.out.println(
                "50 <= Idade < 60: " + faixa4 + " (" + df.format((float) faixa4 / getQuantityElected() * 100) + "%)");
        System.out.println(
                "60 <= Idade : " + faixa5 + " (" + df.format((float) faixa5 / getQuantityElected() * 100) + "%)");

    }

    /*
     * print the candidates name with the number of votes and the party acronym
     */
    public void printCandidatePartyVotes(List<Candidate> candidates) {
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
