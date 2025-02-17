import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

enum Gender {
    MASCULINE,
    FEMININE;
}

enum CandidateSituation {
    ELECTED,
    NOT_ELECTED,
    INVALID;
}

public class Candidate {
    private int cityCode;
    private int jobCode;
    private int candidateNumber;
    private String candidateName;
    private int partyNumber;
    private String partyAcronym;
    private int federationNumber;
    private LocalDate birthDate;
    private CandidateSituation candidateSituation;
    private Gender gender;
    private int candidateVotes;
    private long candidateAge;

    public Candidate(String[] values) {
        this.cityCode = Integer.parseInt(values[0]);
        this.jobCode = Integer.parseInt(values[1]);
        this.candidateNumber = Integer.parseInt(values[2]);
        this.candidateName = values[3];
        this.partyNumber = Integer.parseInt(values[4]);
        this.partyAcronym = values[5];
        this.federationNumber = Integer.parseInt(values[6]);

        String dateStr = values[7];

        // convert string to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.birthDate = LocalDate.parse(dateStr, formatter);

        if (values[8].equals("2") || values[8].equals("3")) {
            this.candidateSituation = CandidateSituation.ELECTED;
        } else if (values[8].equals("-1")) {
            this.candidateSituation = CandidateSituation.INVALID;
        } else {
            this.candidateSituation = CandidateSituation.NOT_ELECTED;
        }

        if (values[9].equals("2")) {
            this.gender = Gender.MASCULINE;
        } else if (values[9].equals("4")) {
            this.gender = Gender.FEMININE;
        }
    }

    /* 
     * create candidates and save them in each political party
     */
    public static HashMap<Integer, Candidate> CreateCandidates(List<String[]> values, HashMap<Integer, PoliticalParty> politicalParties) {
        HashMap<Integer, Candidate> candidates = new HashMap<Integer, Candidate>();

        for (String[] value : values) {
            Candidate candidate = new Candidate(value);
            candidates.put(candidate.candidateNumber, candidate);

            PoliticalParty candidateParty = politicalParties.get(candidate.partyNumber);
            candidateParty.addCandidate(candidate);
        }

        return new HashMap<>(candidates);
    }

    // getters
    public int getCityCode() {
        return cityCode;
    }

    public int getJobCode() {
        return jobCode;
    }

    public int getCandidateNumber() {
        return candidateNumber;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public int getPartyNumber() {
        return partyNumber;
    }

    public String getPartyAcronym() {
        return partyAcronym;
    }

    public int getFederationNumber() {
        return federationNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public CandidateSituation getCandidateSituation() {
        return candidateSituation;
    }

    public Gender getGender() {
        return gender;
    }

    public int getCandidateVotes() {
        return candidateVotes;
    }
    // end getters

    public void incCandidateVotes(int votes) {
        candidateVotes += votes;
    }

    public void setCandidateAge(LocalDate electionDate) {
        candidateAge = ChronoUnit.YEARS.between(birthDate, electionDate);
    }

    public long getCandidateAge() {
        return candidateAge;
    }
}