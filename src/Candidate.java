import java.io.*;
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
    private int cityCode; // codigo do municipio
    private int jobCode; // codigo do cargo
    private int candidateNumber; // numero do candidato
    private String candidateName; // nome do candidato
    private int partyNumber; // numero do partido
    private String partyAcronym; // sigla do partido
    private int federationNumber; // numero da federacao (-1 para partido isolado)
    private String birthDate; // data de nascimento
    private CandidateSituation candidateSituation;// situacao do candidato no turno (2 ou 3 - eleito) (-1 - nao processar)
    private Gender gender; // genero (2 - masculino, 4 - feminino)
    private int candidateVotes; // votos

    
    public Candidate(String[] values) {
        this.cityCode = Integer.parseInt(values[0]);
        this.jobCode = Integer.parseInt(values[1]);
        this.candidateNumber = Integer.parseInt(values[2]);
        this.candidateName = values[3];
        this.partyNumber = Integer.parseInt(values[4]);
        this.partyAcronym = values[5];
        this.federationNumber = Integer.parseInt(values[6]);
        this.birthDate = values[7];
        if (values[8].equals("2") || values[8].equals("3")) {
            this.candidateSituation = CandidateSituation.ELECTED;
        } 
        else if (values[8].equals("-1")) {
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

    public static HashMap<Integer, Candidate> CreateCandidates(List<String[]> values) {
        HashMap<Integer, Candidate> candidates = new HashMap<Integer, Candidate>();

        for (String[] value : values) {
            Candidate candidate = new Candidate(value);
            candidates.put(candidate.candidateNumber, candidate);
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

    public String getBirthDate() {
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

    @Override
    public String toString() {
        return "\n+----------------------------------------+\n" +
               "| Codigo do municipio: " + cityCode + "\n" +
               "| Codigo do cargo: " + jobCode + "\n" +
               "| Numero do candidato: " + candidateNumber + "\n" +
               "| Nome do candidato: " + candidateName + "\n" +
               "| Numero do partido: " + partyNumber + "\n" +
               "| Sigla do partido: " + partyAcronym + "\n" +
               "| Numero da federacao: " + federationNumber + "\n" +
               "| Data de nascimento: " + birthDate + "\n" +
               "| Situacao do candidato: " + candidateSituation + "\n" +
               "| Genero: " + gender + "\n" +
               "| Votos: " + candidateVotes + "\n" +
               "+----------------------------------------+\n";

    }

    public void printCandidate() {
        System.out.println(this.candidateName + " (" + this.partyAcronym + ", " + this.candidateVotes + " votos)");
    }
}