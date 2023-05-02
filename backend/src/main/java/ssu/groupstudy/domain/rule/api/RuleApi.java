package ssu.groupstudy.domain.rule.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssu.groupstudy.domain.rule.domain.Rule;
import ssu.groupstudy.domain.rule.dto.request.CreateRuleRequest;
import ssu.groupstudy.domain.rule.service.RuleService;
import ssu.groupstudy.domain.study.domain.Study;
import ssu.groupstudy.domain.study.dto.reuqest.CreateStudyRequest;
import ssu.groupstudy.global.dto.DataResponseDto;

@RestController
@RequestMapping("/rule")
@AllArgsConstructor
@Tag(name = "Rule", description = "규칙 API")
public class RuleApi {
    private final RuleService ruleService;

    @Operation(summary = "새로운 규칙 생성")
    @PostMapping("")
    public DataResponseDto register(@Valid @RequestBody CreateRuleRequest dto){
        Rule rule = ruleService.createRule(dto);

        return DataResponseDto.of("rule", rule);
    }
}