package com.app.gamereview.service;

import com.app.gamereview.dto.request.vote.CreateVoteRequestDto;
import com.app.gamereview.dto.request.vote.GetAllVotesFilterRequestDto;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.enums.VoteType;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Review;
import com.app.gamereview.model.User;
import com.app.gamereview.model.Vote;
import com.app.gamereview.repository.ReviewRepository;
import com.app.gamereview.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    private final ReviewRepository reviewRepository;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;

    @Autowired
    public VoteService(
            VoteRepository voteRepository,
            ReviewRepository reviewRepository,
            MongoTemplate mongoTemplate,
            ModelMapper modelMapper
    ) {
        this.voteRepository = voteRepository;
        this.reviewRepository = reviewRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;

        modelMapper.addMappings(new PropertyMap<CreateVoteRequestDto, Vote>() {
            @Override
            protected void configure() {
                map().setTypeId(source.getTypeId());
                skip().setId(null); // Exclude id from mapping
            }
        });
    }

    public List<Vote> getAllVotes(GetAllVotesFilterRequestDto filter) {
        Query query = new Query();
        if (filter.getVoteType() != null) {
            query.addCriteria(Criteria.where("voteType").is(filter.getVoteType()));
        }
        if (filter.getTypeId() != null) {
            query.addCriteria(Criteria.where("typeId").is(filter.getTypeId()));
        }
        if (filter.getChoice() != null) {
            query.addCriteria(Criteria.where("choice").is(filter.getChoice()));
        }
        if (filter.getVotedBy() != null) {
            query.addCriteria(Criteria.where("votedBy").is(filter.getVotedBy()));
        }
        if (!filter.getWithDeleted()) {
            query.addCriteria(Criteria.where("isDeleted").is(filter.getWithDeleted()));
        }

        return mongoTemplate.find(query, Vote.class);
    }

    public Vote getVote(String voteId){
        Optional<Vote> vote = voteRepository.findById(voteId);

        if(vote.isEmpty() || vote.get().getIsDeleted()){
            throw new ResourceNotFoundException("Vote not found");
        }

        return vote.get();
    }

    public Vote addVote(CreateVoteRequestDto requestDto, User user){
        Vote voteToCreate = modelMapper.map(requestDto, Vote.class);
        voteToCreate.setVotedBy(user.getId());

        GetAllVotesFilterRequestDto filter = new GetAllVotesFilterRequestDto();
        filter.setVotedBy(user.getId());
        filter.setTypeId(requestDto.getTypeId());
        filter.setVoteType(requestDto.getVoteType());
        List<Vote> prevVote = getAllVotes(filter);

        VoteChoice choice = VoteChoice.valueOf(requestDto.getChoice());

        // if user has voted this same thing before with the SAME CHOICE
        if(!prevVote.isEmpty() &&
                prevVote.get(0).getChoice().name().equals(requestDto.getChoice())) {

           if(requestDto.getVoteType().equals(VoteType.REVIEW.name())){
               // delete previous vote
               Review review = reviewRepository.findById(requestDto.getTypeId()).get();
               review.deleteVote(choice);
               reviewRepository.save(review);
               deleteVote(prevVote.get(0).getId());
               return prevVote.get(0);
           }

           // TODO same logic will be extended to other models that have voting mechanism

        }
        // if user has voted this same thing before but CHANGED HIS CHOICE
        else if(!prevVote.isEmpty() &&
                !prevVote.get(0).getChoice().name().equals(requestDto.getChoice())){

            if(requestDto.getVoteType().equals(VoteType.REVIEW.name())){
                // delete previous vote
                Review review = reviewRepository.findById(requestDto.getTypeId()).get();
                review.deleteVote(prevVote.get(0).getChoice());
                deleteVote(prevVote.get(0).getId());

                // add new vote
                review.addVote(choice);

                reviewRepository.save(review);
                return voteRepository.save(voteToCreate);
            }

            // TODO same logic will be extended to other models that have voting mechanism

        }
        // user votes something first time
        else{

            if(requestDto.getVoteType().equals(VoteType.REVIEW.name())){
                // add new vote
                Review review = reviewRepository.findById(requestDto.getTypeId()).get();
                review.addVote(choice);
                reviewRepository.save(review);
                return voteRepository.save(voteToCreate);
            }

            // TODO same logic will be extended to other models that have voting mechanism

        }

        return voteToCreate;
    }

    public Boolean deleteVote(String voteId){
        Optional<Vote> findResult = voteRepository.findById(voteId);

        if(findResult.isEmpty()){
            throw new ResourceNotFoundException("Vote not found");
        }

        voteRepository.deleteById(voteId);

        return true;
    }

}
