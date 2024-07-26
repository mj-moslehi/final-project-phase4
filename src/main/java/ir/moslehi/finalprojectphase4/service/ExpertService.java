package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.dto.search.AdminSearchingRequest;
import ir.moslehi.finalprojectphase4.email.EmailSender;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.exception.NotValidInput;
import ir.moslehi.finalprojectphase4.model.*;
import ir.moslehi.finalprojectphase4.model.enums.ExpertStatus;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.enums.Role;
import ir.moslehi.finalprojectphase4.repository.ExpertRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ExpertService {

    private final ExpertRepository expertRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SubServiceService subServiceService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final PersonService personService;

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Expert expert) {
        Date now = new Date();
        personService.findByEmail(expert.getEmail());
        expert.setExpertStatus(ExpertStatus.New);
        expert.setDateOfSignUp(now);
        expert.setScore(0.0);
        expert.setValidity(0L);
        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        expert.setRole(Role.ROLE_EXPERT);
        expertRepository.save(expert);
    }

    @Transactional
    public String confirmToken(String token) {
        Expert expert = findByEmail(checkConfirmToken(token).getExpert().getEmail());
        expert.setExpertStatus(ExpertStatus.WAITING_FOR_CONFIRMED);
        expertRepository.save(expert);
        return "confirmed";
    }

    public ConfirmationToken checkConfirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        return confirmationToken;
    }

    public String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public Expert register(Expert expert) {

        save(expert);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .expert(expert)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/expert/registration/confirm?token=" + token;
        emailSender.send(expert.getEmail(), buildEmail(expert.getFirstname(), link));

        return expert;
    }

    public void readImage(Expert expert, MultipartFile file) throws IOException {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jpg"))
            throw new NotValidInput("file must be jpg");
        if (file.getSize() > (300 * 1024))
            throw new NotValidInput("File must be les than 300 KB");
        expert.setImage(file.getBytes());
        expertRepository.save(expert);
    }

    public Expert update(Expert expert, String email) {
        Expert foudnExpert = findByEmail(email);
        foudnExpert.setPassword(passwordEncoder.encode(expert.getPassword()));
        personService.findByEmail(expert.getEmail());
        foudnExpert.setEmail(expert.getEmail());
        return expertRepository.save(foudnExpert);
    }

    public void writeImage(String pathString, String email) {
        Path path = Paths.get(pathString);
        Expert expert = findByEmail(email);
        try (FileOutputStream fos = new FileOutputStream(path.toString())) {
            fos.write(expert.getImage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Expert confirmedExpertStatus(String email) {
        Expert foundExpert = findByEmail(email);
        if (foundExpert.getExpertStatus().equals(ExpertStatus.WAITING_FOR_CONFIRMED)) {
            foundExpert.setExpertStatus(ExpertStatus.CONFIRMED);
            foundExpert.setEnabled(true);
            return expertRepository.save(foundExpert);
        } else throw new NotValidInput("the expert with id : " + foundExpert.getId() + " hasn't confirmed email");
    }

    public void updateScoreWithDelayHour(Expert expert, Double hours) {
        expert.setScore(expert.getScore() - hours);
        if (expert.getScore() < 0)
            expert.setEnabled(false);
        expertRepository.save(expert);
    }

    public void updateScoreWithCommentScore(Expert expert, Double score) {
        if (expert.getScore() == 0.0) {
            expert.setScore(score);
            expertRepository.save(expert);
        } else if (expert.getScore() > 0.0) {
            expert.setScore((expert.getScore() + score) / 2);
            expertRepository.save(expert);
        } else if (expert.getScore() < 0) {
            expert.setEnabled(false);
            expertRepository.save(expert);
        }
    }

    public void updateValidity(Expert expert, Long price) {
        expert.setValidity(expert.getValidity() + price);
        expertRepository.save(expert);
    }

    public Expert findByEmail(String email) {
        return expertRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("expert wasn't found with email : " + email)
        );
    }

    public List<Expert> expertSearch(AdminSearchingRequest adminSearchingRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Expert> expertQuery = builder.createQuery(Expert.class);
        Root<Expert> root = expertQuery.from(Expert.class);
        List<Predicate> predicates = new ArrayList<>();

        if (adminSearchingRequest.expertStatus() != null)
            predicates.add(builder.equal(root.get("expertStatus"), adminSearchingRequest.expertStatus()));
        if (adminSearchingRequest.score() != null)
            predicates.add(builder.equal(root.get("score"), adminSearchingRequest.score()));
        if (adminSearchingRequest.userEnabled() != null)
            predicates.add(builder.equal(root.get("enabled"), adminSearchingRequest.userEnabled()));

        personsInfoInSearching(adminSearchingRequest, builder, root.get("role")
                , root.get("firstname"), root.get("lastname"), root.get("email"),
                root.get("validity"), root.get("dateOfSignUp"), predicates);

        expertQuery.where(builder.and(predicates.toArray(predicates.toArray(new Predicate[]{}))));

        List<Expert> expertList = entityManager.createQuery(expertQuery).getResultList();
        List<Expert> newList = new ArrayList<>();

        if (adminSearchingRequest.orderNumForUser() != null)
            expertList.retainAll(orderNumInSearch(adminSearchingRequest, builder, expertList, newList));

        if (adminSearchingRequest.expertSubServiceName() != null)
            expertList.retainAll(subServiceNameInSearching(adminSearchingRequest, builder, expertList, newList));

        if (adminSearchingRequest.orderNumInDoneForUser() != null)
            expertList.retainAll(orderNumInDoneInSearching(adminSearchingRequest, builder, expertList, newList));

        return expertList;
    }

    private List<Expert> orderNumInSearch(AdminSearchingRequest adminSearchingRequest, CriteriaBuilder builder,
                                          List<Expert> expertList, List<Expert> newList) {
        CriteriaQuery<Object[]> orderQuery = builder.createQuery(Object[].class);
        Root<Orders> rootOrder = orderQuery.from(Orders.class);
        orderQuery.multiselect(
                rootOrder.get("expert"),
                builder.count(rootOrder)
        );

        orderQuery.groupBy(rootOrder.get("expert"));
        orderQuery.having(builder.gt(builder.count(rootOrder), adminSearchingRequest.orderNumForUser()));
        return getExperts(expertList, newList, orderQuery);
    }

    private List<Expert> orderNumInDoneInSearching(AdminSearchingRequest adminSearchingRequest, CriteriaBuilder builder,
                                                   List<Expert> expertList, List<Expert> newList) {
        CriteriaQuery<Object[]> orderQuery = builder.createQuery(Object[].class);
        Root<Orders> rootOrder = orderQuery.from(Orders.class);
        Predicate orderStatusPredicate = builder.equal(rootOrder.get("orderStatus"), OrderStatus.DONE);
        orderQuery.multiselect(
                rootOrder.get("expert"),
                builder.count(rootOrder)
        );
        orderQuery.where(orderStatusPredicate);
        orderQuery.groupBy(rootOrder.get("expert"));
        orderQuery.having(builder.gt(builder.count(rootOrder), adminSearchingRequest.orderNumInDoneForUser()));
        return getExperts(expertList, newList, orderQuery);
    }

    private List<Expert> subServiceNameInSearching(AdminSearchingRequest adminSearchingRequest, CriteriaBuilder builder,
                                                   List<Expert> expertList, List<Expert> newList) {
        SubService subService = subServiceService.findByName(adminSearchingRequest.expertSubServiceName());
        CriteriaQuery<Object[]> expertSubServiceQuery = builder.createQuery(Object[].class);
        Root<ExpertSubService> expertSubServiceRoot = expertSubServiceQuery.from(ExpertSubService.class);
        expertSubServiceQuery.select(expertSubServiceRoot.get("expert"))
                .where(builder.equal(expertSubServiceRoot.get("subService"), subService));
        return getExperts(expertList, newList, expertSubServiceQuery);

    }

    public void personsInfoInSearching(AdminSearchingRequest adminSearchingRequest, CriteriaBuilder builder,
                                       jakarta.persistence.criteria.Path<Object> role,
                                       jakarta.persistence.criteria.Path<Object> firstname,
                                       jakarta.persistence.criteria.Path<Object> lastname,
                                       jakarta.persistence.criteria.Path<Object> email,
                                       jakarta.persistence.criteria.Path<Object> validity,
                                       jakarta.persistence.criteria.Path<Date> dateOfSignUp,
                                       List<Predicate> predicates) {

        if (adminSearchingRequest.role() != null)
            predicates.add(builder.equal(role, adminSearchingRequest.role()));
        if (adminSearchingRequest.firstname() != null)
            predicates.add(builder.equal(firstname, adminSearchingRequest.firstname()));
        if (adminSearchingRequest.lastname() != null)
            predicates.add(builder.equal(lastname, adminSearchingRequest.lastname()));
        if (adminSearchingRequest.email() != null)
            predicates.add(builder.equal(email, adminSearchingRequest.email()));
        if (adminSearchingRequest.dateOfSignUpStart() != null)
            predicates.add(builder.greaterThanOrEqualTo(dateOfSignUp, adminSearchingRequest.dateOfSignUpStart()));
        if (adminSearchingRequest.dateOfSignUpEnd() != null)
            predicates.add(builder.lessThanOrEqualTo(dateOfSignUp, adminSearchingRequest.dateOfSignUpEnd()));
        if (adminSearchingRequest.validity() != null)
            predicates.add(builder.equal(validity, adminSearchingRequest.validity()));
    }

    private List<Expert> getExperts(List<Expert> expertList, List<Expert> newList, CriteriaQuery<Object[]> expertSubServiceQuery) {
        List<Object[]> results = entityManager.createQuery(expertSubServiceQuery).getResultList();
        for (Object[] result : results) {
            Expert expert = (Expert) result[0];
            newList.add(expert);
        }
        expertList.retainAll(newList);
        return expertList;
    }


    public List<Orders> ordersHistoryExpert(String email, String orderStatus) {
        Arrays.stream(OrderStatus.values()).anyMatch((t) -> t.name().equals(orderStatus));
        Expert expert = findByEmail(email);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> orderCriteriaQuery = builder.createQuery(Orders.class);
        Root<Orders> root = orderCriteriaQuery.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("expert"), expert));
        predicates.add(builder.equal(root.get("orderStatus"), orderStatus));
        orderCriteriaQuery.where(builder.and(predicates.toArray(predicates.toArray(new Predicate[]{}))));
        return entityManager.createQuery(orderCriteriaQuery).getResultList();
    }

}
