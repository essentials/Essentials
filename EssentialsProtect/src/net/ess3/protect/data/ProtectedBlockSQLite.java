package net.ess3.protect.data;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ProtectedBlockSQLite extends ProtectedBlockJDBC
{
	public ProtectedBlockSQLite(final String url) throws PropertyVetoException
	{
		super("org.sqlite.JDBC", url);
	}
	private static final String QueryCreateTable =
								"CREATE TABLE IF NOT EXISTS EssentialsProtect ("
								+ "worldName TEXT ,playerName TEXT, "
								+ "x NUMERIC, y NUMERIC, z NUMERIC)";

	@Override
	protected PreparedStatement getStatementCreateTable(final Connection conn) throws SQLException
	{
		return conn.prepareStatement(QueryCreateTable);
	}
	private static final String QueryUpdateFrom2_0Table =
								"CREATE INDEX IF NOT EXISTS position ON EssentialsProtect ("
								+ "worldName, x, z, y)";

	@Override
	protected PreparedStatement getStatementUpdateFrom2_0Table(final Connection conn) throws SQLException
	{
		return conn.prepareStatement(QueryUpdateFrom2_0Table);
	}
	private static final String QueryDeleteAll = "DELETE FROM EssentialsProtect;";

	@Override
	protected PreparedStatement getStatementDeleteAll(final Connection conn) throws SQLException
	{
		return conn.prepareStatement(QueryDeleteAll);
	}
	private static final String QueryInsert =
								"INSERT INTO EssentialsProtect (worldName, x, y, z, playerName) VALUES (?, ?, ?, ?, ?);";

	@Override
	protected PreparedStatement getStatementInsert(final Connection conn, final String world,
												   final int x, final int y, final int z, final String playerName) throws SQLException
	{
		final PreparedStatement ps = conn.prepareStatement(QueryInsert);
		ps.setString(1, world);
		ps.setInt(2, x);
		ps.setInt(3, y);
		ps.setInt(4, z);
		ps.setString(5, playerName);
		return ps;
	}
	private static final String QueryPlayerCountByLocation =
								"SELECT COUNT(playerName), SUM(playerName = ?) FROM EssentialsProtect "
								+ "WHERE worldName = ? AND x = ? AND y = ? AND z = ? GROUP BY x;";

	@Override
	protected PreparedStatement getStatementPlayerCountByLocation(final Connection conn, final String world,
																  final int x, final int y, final int z, final String playerName) throws SQLException
	{
		final PreparedStatement ps = conn.prepareStatement(QueryPlayerCountByLocation);
		ps.setString(1, playerName);
		ps.setString(2, world);
		ps.setInt(3, x);
		ps.setInt(4, y);
		ps.setInt(5, z);
		return ps;
	}
	private static final String QueryPlayersByLocation =
								"SELECT playerName FROM EssentialsProtect WHERE worldname = ? AND x = ? AND y = ? AND z = ?;";

	@Override
	protected PreparedStatement getStatementPlayersByLocation(final Connection conn, final String world,
															  final int x, final int y, final int z) throws SQLException
	{
		final PreparedStatement ps = conn.prepareStatement(QueryPlayersByLocation);
		ps.setString(1, world);
		ps.setInt(2, x);
		ps.setInt(3, y);
		ps.setInt(4, z);
		return ps;
	}
	private static final String QueryDeleteByLocation =
								"DELETE FROM EssentialsProtect WHERE worldName = ? AND x = ? AND y = ? AND z = ?;";

	@Override
	protected PreparedStatement getStatementDeleteByLocation(final Connection conn, final String world,
															 final int x, final int y, final int z) throws SQLException
	{
		final PreparedStatement ps = conn.prepareStatement(QueryDeleteByLocation);
		ps.setString(1, world);
		ps.setInt(2, x);
		ps.setInt(3, y);
		ps.setInt(4, z);
		return ps;
	}
	private static final String QueryAllBlocks =
								"SELECT worldName, x, y, z, playerName FROM EssentialsProtect;";

	@Override
	protected PreparedStatement getStatementAllBlocks(final Connection conn) throws SQLException
	{
		return conn.prepareStatement(QueryAllBlocks);
	}
}
