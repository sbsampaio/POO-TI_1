import java.io.*;
import java.util.*;

import javax.lang.model.type.NullType;

public class PoliticalParty {
    private String name;
    private String acronym;
    private int number;

    private int nominalVotes;
    private int captionVotes;
    private int electedCandidates;
    private List<Candidate> candidates;

    public PoliticalParty(String[] values) {
        this.name = values[2];
        this.acronym = values[1];
        this.number = Integer.parseInt(values[0]);
        this.candidates = new ArrayList<Candidate>();
    }

    public String getName() {
        return name;
    }

    public String getPartyAcronym() {
        return acronym;
    }

    public int getNumber() {
        return number;
    }

    public int getNominalVotes() {
        return nominalVotes;
    }

    public int getCaptionVotes() {
        return captionVotes;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public int getElectedCandidates() {
        return electedCandidates;
    }

    public int getTotalVotes() {
        return nominalVotes + captionVotes;
    }

    public void addNominalVotes(int votes) {
        nominalVotes += votes;
    }

    public void addCaptionVotes(int votes) {
        captionVotes += votes;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    // public void incrementElectedCandidates() {
    // electedCandidates++;
    // }

    public Candidate getMostVotedCandidate() {
        return candidates.get(0);
    }

    public Candidate getLeastVotedCandidate() {
        int idx = candidates.size() - 1;
        Candidate c = candidates.get(idx);
        while (c.getCandidateVotes() == 0 && idx > 0) {
            idx--;
            c = candidates.get(idx);
        }
        return c;
        // return candidates.get(candidates.size() - 1);
    }

    public Candidate getLeastVotedElectedCandidate() {
        return candidates.get(getElectedCandidates());
    }

    public void computeElectedCandidates() {
        for (Candidate candidate : candidates) {
            if (candidate.getCandidateSituation() == CandidateSituation.ELECTED) {
                electedCandidates++;
            }
        }
    }

    public static HashMap<Integer, PoliticalParty> readPoliticalParties(String filePath) {
        HashMap<Integer, PoliticalParty> politicalParties = new HashMap<Integer, PoliticalParty>();
        int KEYS_NUM = 3;

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file, "ISO-8859-1");

            // discards first line of csv
            if (scanner.hasNextLine())
                scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] row = scanner.nextLine().split(";");

                // select the following columns: NR_PARTIDO, SG_PARTIDO, NM_PARTIDO
                String[] selectedValues = {
                        row[25], row[26], row[27]
                };

                // remove all the quotes (and double quotes) from the values
                for (int i = 0; i < KEYS_NUM; i++) {
                    selectedValues[i] = selectedValues[i].trim().replaceAll("^\"|\"$", "");
                }

                PoliticalParty party = new PoliticalParty(selectedValues);
                politicalParties.putIfAbsent(Integer.parseInt(selectedValues[0]), party);

            }
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return politicalParties;
    }

}