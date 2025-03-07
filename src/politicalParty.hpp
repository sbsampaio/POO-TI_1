#if !defined POLITICALPARTY_HPP
#define POLITICALPARTY_HPP

#include "candidate.hpp"

#include <string>
#include <iostream>
#include <vector>
#include <list>

using namespace std;

class PoliticalParty
{
    string name;
    string acronym;
    int number;

    int nominalVotes;
    int captionVotes;
    int electedCandidates;
    list<Candidate> candidates;

public:
    PoliticalParty(const vector<string> &values);

    string getName();

    string getPartyAcronym();

    int getNumber();

    int getNominalVotes();

    int getCaptionVotes();

    list<Candidate> getCandidates();

    int getElectedCandidates();

    int getTotalVotes();

    void addNominalVotes(const int &votes);

    void addCaptionVotes(const int &votes);

    void addCandidate(const Candidate &candidate);

    Candidate getMostVotedCandidate();

    Candidate getLeastVotedCandidate();

    Candidate getLeastVotedElectedCandidate();

    void computeElectedCandidates();

    map<int, PoliticalParty> readPoliticalParties(const string &filePath);
};

#endif