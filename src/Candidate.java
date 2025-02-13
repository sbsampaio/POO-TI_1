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

    // essa função aqui pode ser private?
    public Candidate(int cityCode, int jobCode, int candidateNumber, String candidateName, 
                    int partyNumber, String partyAcronym, int federationNumber, 
                    String birthDate, CandidateSituation candidateSituation, Gender gender)
    {
        this.cityCode = cityCode;
        this.jobCode = jobCode;
        this.candidateNumber = candidateNumber;
        this.candidateName = candidateName;
        this.partyNumber = partyNumber;
        this.partyAcronym = partyAcronym;
        this.federationNumber = federationNumber;
        this.birthDate = birthDate;
        this.candidateSituation = candidateSituation;
        this.gender = gender;
        this.candidateVotes = 0;
    }

    public static Candidate CreateCandidate(String[] values) {

        int cityCode = Integer.parseInt(values[0].replaceAll("\"", ""));
        int jobCode = Integer.parseInt(values[1].replaceAll("\"", ""));
        int candidateNumber = Integer.parseInt(values[2].replaceAll("\"", ""));
        String candidateName = values[3];
        int partyNumber = Integer.parseInt(values[4].replaceAll("\"", ""));
        String partyAcronym = values[5];
        int federationNumber = Integer.parseInt(values[6].replaceAll("\"", ""));
        String birthDate = values[7];
        CandidateSituation candidateSituation = null;
        if (values[8].equals("1")) {
            candidateSituation = CandidateSituation.ELECTED;
        } else if (values[8].equals("4")) {
            candidateSituation = CandidateSituation.NOT_ELECTED;
        } else if (values[8].equals("-1")) {
            candidateSituation = CandidateSituation.INVALID;
        }
        Gender gender = null;
        if (values[9].equals("2")) {
            gender = Gender.MASCULINE;
        } else if (values[9].equals("4")) {
            gender = Gender.FEMININE;
        }

        Candidate newCandidate = new Candidate(cityCode, jobCode, candidateNumber, candidateName, partyNumber, partyAcronym, federationNumber, birthDate, candidateSituation, gender);

        return newCandidate;
    }

    public static HashMap<Integer, Candidate> CreateCandidates(List<String[]> values) {
        HashMap<Integer, Candidate> candidates = new HashMap<Integer, Candidate>();

        for (String[] value : values) {
            Candidate candidate = CreateCandidate(value);
            System.out.println(candidate);
            candidates.put(candidate.candidateNumber, candidate);
        }

        return candidates;
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
    // end getters

    // essa função é segura?
    public void addCandidateVotes(HashMap<Integer, Candidate> candidates, int candidateNumber, int candidateVotes) {
        Candidate candidate = candidates.get(candidateNumber);
        candidate.candidateVotes += candidateVotes;
    }

    @Override
    public String toString() {
        return "+----------------------------------------+\n" +
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
               "| Votos: " + candidateVotes + "\n";
    }
}