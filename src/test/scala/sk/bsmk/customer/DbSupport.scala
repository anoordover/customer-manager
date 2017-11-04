package sk.bsmk.customer

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.jooq.{DSLContext, SQLDialect}
import org.jooq.impl.DSL
import sk.bsmk.customer.repository.CustomerRepository

trait DbSupport {

  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl("jdbc:h2:file:./build/customer")
  hikariConfig.setUsername("sa")

  private val datasource = new HikariDataSource(hikariConfig)

  protected val dsl: DSLContext = DSL.using(datasource, SQLDialect.H2)

  protected val repository = CustomerRepository(dsl)

}
