package com.fintrack.domain.usecase

import com.fintrack.domain.model.Money

class ParseMoneyUseCase {
    operator fun invoke(input: String): Result<Money> = Money.parse(input)
}
