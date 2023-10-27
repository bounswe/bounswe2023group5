package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Getter
@Document(collection = "ResetCodes")
public class ResetCode extends BaseModel {

	private final String code;

	@Indexed(unique = true) // Ensures a unique constraint on userId field
	private final String userId; // ID of the associated user

	private final Date expirationDate;

	public ResetCode(String code, String userId, Date expirationDate) {
		this.code = code;
		this.userId = userId;
		this.expirationDate = expirationDate;
	}
}
