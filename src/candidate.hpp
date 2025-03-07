#if !defined CANDIDATE_HPP
#define CANDIDATE_HPP

#include "politicalParty.hpp"

#include <string>
#include <iostream>
#include <ctime>
#include <vector>
#include <map>
#include <sstream>

using namespace std;

enum Gender
{
    MASCULINE,
    FEMININE
};

enum CandidateSituation
{
    ELECTED,
    NOT_ELECTED,
    INVALID
};

class Candidate
{
    int cityCode;
    int jobCode;
    int candidateNumber;
    string candidateName;
    int partyNumber;
    string partyAcronym;
    int federationNumber;
    tm birthDate;
    CandidateSituation candidateSituation;
    Gender gender;
    int candidateVotes;
    long candidateAge;

    int mostVotedPosition;

public:
    Candidate(const vector<string> &values);

    map<int, Candidate> CreateCandidates(
        const vector<vector<string> > &values,
        const map<int, PoliticalParty> &politicalParties);

    int getCityCode();

    int getJobCode();

    int getCandidateNumber();

    string getCandidateName();

    int getPartyNumber();

    string getPartyAcronym();

    int getFederationNumber();

    tm getBirthDate();

    CandidateSituation getCandidateSituation();

    Gender getGender();

    int getCandidateVotes();

    void incCandidateVotes(const int &votes);

    void setCandidateAge(const tm &electionDate);

    long getCandidateAge();

    int getMostVotedPosition();

    void setMostVotedPosition(const int &mostVotedPosition);
};

#endif