import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

public class ElectionReport {
    private CandidateFileReader candidateData;
    private ElectionFileReader votingData;
    private HashMap<Integer, Candidate> candidates;
    private HashMap<Integer, PoliticalParty> politicalParties;
    private LocalDate electionDate;

    public ElectionReport(CandidateFileReader candidateData, ElectionFileReader votingData, String candidateDataPath, LocalDate electionDate) {
        try {
            this.candidateData = candidateData;
            this.votingData = votingData;
            this.politicalParties = PoliticalParty.readPoliticalParties(candidateDataPath);
            this.candidates = Candidate.CreateCandidates(candidateData.getValues(), politicalParties);
            this.electionDate = electionDate;

            votingData.ComputeVotes(this.candidates, this.politicalParties);

            printQuantityElected();
            printElectedCandidates();
            printMostVotedCandidates();
            printMajorityElectedCandidates();
            printElectedWithProportionalBenefit();
            printPoliticalPartyData();
            printfMostAndLeastPartyCandidates();
            printElectedByAge(electionDate);
            printElectedByGender();
            printVoteData();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * 1-
     * print the quantity of elected candidates
     */
    public void printQuantityElected() {
        System.out.println("Número de vagas: " + getQuantityElected());
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
     * 2-
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

        // return only the limit, that is the amount of elected candidates
        return mostVotedCandidates.subList(0, Math.min(limit, mostVotedCandidates.size()));
    }

    /*
     * 3-
     * print the most voted candidates
     */
    public void printMostVotedCandidates() {
        System.out
                .println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");

        List<Candidate> mostVotedCandidates = getMostVotedCandidates(getQuantityElected());

        printCandidatePartyVotes(mostVotedCandidates);
    }

    /*
     * 4-
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
     * 5-
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
     * 6-
     * print all the political partys ordered by votes (decrescent)
     * shows the amount of nominal and legend votes, and the amount of
     * elected candidates
     */
    public void printPoliticalPartyData() {
        System.out.println("\nVotação dos partidos e número de candidatos eleitos:");


        for (PoliticalParty party : politicalParties.values()) {
            party.computeElectedCandidates();
        }

        List<PoliticalParty> politicalPartiesList = new ArrayList<>(politicalParties.values());

        politicalPartiesList.sort(Comparator
                .comparing(PoliticalParty::getTotalVotes, Comparator.reverseOrder())
                .thenComparing(PoliticalParty::getNumber));

        NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));

        int counter = 0;
        for (PoliticalParty party : politicalPartiesList) {
            counter++;
            System.out.println(counter + " - " + party.getPartyAcronym() + " - " + party.getNumber() +
                    ", " + brFormat.format(party.getTotalVotes()) + " votos (" + brFormat.format(party.getNominalVotes()) + " nominais e " +
                    brFormat.format(party.getCaptionVotes()) + " de legenda), " + party.getElectedCandidates() + " candidatos eleitos");
        }
    }

    /*
     * 7-
     * print most and least voted candidates of each political party
     */
    public void printfMostAndLeastPartyCandidates() {
        System.out.println("\nPrimeiro e último colocados de cada partido:");

        List<PoliticalParty> politicalPartiesList = new ArrayList<>(politicalParties.values());

        politicalPartiesList.sort(Comparator.comparing(
                party -> party.getCandidates().stream()
                        .mapToInt(Candidate::getCandidateVotes)
                        .max()
                        .orElse(0),
                Comparator.reverseOrder()));

        NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        

        int counter = 0;
        for (PoliticalParty party : politicalPartiesList) {

            if (party.getTotalVotes() > 0) {
                party.getCandidates().sort(Comparator
                        .comparing(Candidate::getCandidateVotes, Comparator.reverseOrder())
                        .thenComparing(Candidate::getCandidateAge));

                Candidate mostVoted = party.getMostVotedCandidate();
                Candidate leastVoted = party.getLeastVotedCandidate();

                counter++;
                System.out.println(counter + " - " + party.getPartyAcronym() + " - " + party.getNumber() + ", " +
                        mostVoted.getCandidateName() + " (" + mostVoted.getCandidateNumber() + ", "
                        + brFormat.format(mostVoted.getCandidateVotes()) + " votos) / " +
                        leastVoted.getCandidateName() + " (" + leastVoted.getCandidateNumber() + ", "
                        + brFormat.format(leastVoted.getCandidateVotes()) + " votos)");
            }
        }
    }

    /*
     * 8-
     * print the data about the age range of the elected candidates
     */
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
     * 9-
     * stats of the gender distribuition of the elected candidates
     */
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

    /* 
     * print totalvotes, nominalvotes and captionvotes
     */
    public void printVoteData() {

        int totalVotes = 0, nominalVotes = 0, captionVotes = 0;

        for (PoliticalParty party : politicalParties.values()) {
            nominalVotes += party.getNominalVotes();
            captionVotes += party.getCaptionVotes();
            totalVotes += party.getTotalVotes();
        }

        NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        System.out.println("\nTotal de votos válidos: " + brFormat.format(totalVotes));
        System.out.println("Total de votos nominais: " + brFormat.format(nominalVotes) + " (" + df.format((float)nominalVotes / totalVotes * 100) + "%)");
        System.out
                .println("Total de votos de legenda: " + brFormat.format(captionVotes) + " (" + df.format((float)captionVotes / totalVotes * 100) + "%)");
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
