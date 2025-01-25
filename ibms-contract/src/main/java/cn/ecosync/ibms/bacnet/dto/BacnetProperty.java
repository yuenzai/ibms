package cn.ecosync.ibms.bacnet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class BacnetProperty {
    public static final String REGEXP = "(\\d+)(?:\\[(\\d+)\\])?";
    private static final Pattern PATTERN = Pattern.compile(REGEXP);
    public static final BacnetProperty PROPERTY_PRESENT_VALUE = new BacnetProperty(BacnetPropertyId.PROP_PRESENT_VALUE);

    @NotNull
    private BacnetPropertyId propertyIdentifier;
    private Integer propertyArrayIndex;

    protected BacnetProperty() {
    }

    public BacnetProperty(BacnetPropertyId propertyIdentifier) {
        this(propertyIdentifier, null);
    }

    public BacnetProperty(BacnetPropertyId propertyIdentifier, Integer propertyArrayIndex) {
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
    }

    public Optional<Integer> getPropertyArrayIndex() {
        return Optional.ofNullable(propertyArrayIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BacnetProperty)) return false;
        BacnetProperty that = (BacnetProperty) o;
        return propertyIdentifier == that.propertyIdentifier && Objects.equals(propertyArrayIndex, that.propertyArrayIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyIdentifier, propertyArrayIndex);
    }

    @Override
    public String toString() {
        String idStr = String.valueOf(propertyIdentifier.getCode());
        if (propertyArrayIndex != null) idStr += "[" + propertyArrayIndex + "]";
        return idStr;
    }

    public static BacnetProperty fromString(String propertyString) {
        Matcher matcher = PATTERN.matcher(propertyString);
        Assert.isTrue(matcher.matches(), "REGEXP not match");
        BacnetPropertyId propertyIdentifier = BacnetPropertyId.of(Integer.parseInt(matcher.group(1)));
        Integer propertyArrayIndex = Optional.ofNullable(matcher.group(2))
                .map(Integer::parseInt)
                .orElse(null);
        return new BacnetProperty(propertyIdentifier, propertyArrayIndex);
    }
}
