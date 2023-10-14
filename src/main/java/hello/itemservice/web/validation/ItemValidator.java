package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    // 지정된 클래스가 이 유효성 검사기를 지원하는지 확인
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    // 주어진 객체의 유효성을 검사
    @Override
    public void validate(Object target, Errors errors) {
        // 주어진 객체를 Item으로 캐스팅
        Item item = (Item) target;

        // 아이템 이름 필드가 비어 있거나 공백 문자로만 이루어져 있으면 "required" 에러 발생
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");

        // 아이템의 가격이 범위를 벗어나면 "range" 에러 발생
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        // 아이템 수량이 최대 허용값을 초과하면 "max" 에러 발생
        if (item.getQuantity() == null || item.getQuantity() > 10000) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 아이템의 가격과 수량이 모두 존재하는 경우, 두 값을 곱한 결과가 최소값보다 작으면 "totalPriceMin" 에러 발생
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}